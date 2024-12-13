import { openEditPage } from "../util_page.js";

let bookmarkCache = []; // 북마크 데이터를 저장할 전역 변수
let tagCache = []; // 태그 데이터를 저장할 전역 변수

document.addEventListener("DOMContentLoaded", function () {
  const saveBookmarkBtn = document.querySelector(".save-div");
  const tagContainer = document.querySelector(".tag-div");
  const bookmarkList = document.querySelector(".thumnail-1-div");
  const goToSearchBtn = document.querySelector(".search-div");

  let userEmail;
  chrome.storage.sync.get("userData", ({ userData }) => {
    if (userData && userData.email) {
      userEmail = userData.email; // Retrieve the email dynamically
      console.log("User email:", userEmail);
      initializeData();
    } else {
      console.error("User email not found in chrome.storage.sync");
    }
  });


  // 검색 버튼 클릭 시 search_page.html로 이동
  goToSearchBtn.addEventListener("click", function () {
    chrome.tabs.create({
      url: chrome.runtime.getURL("search_page.html"),
    });
  });

  // "북마크 저장하기" 버튼 클릭 이벤트
  saveBookmarkBtn.addEventListener("click", function () {
    chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
      const currentTab = tabs[0];

      // GPT API 호출하여 태그 생성
      fetchGPTTags(currentTab.url, currentTab.title)
        .then((tags) => {
          // 북마크 데이터 생성
          const bookmark = {
            title: currentTab.title,
            url: currentTab.url,
            tags: tags,
          };

          console.log("생성할 북마크와 요소 출력", bookmark);
          openEditPage(bookmark, "save");
          window.close(); // 팝업 창을 닫음

        })
        .catch((error) => {
          console.error("태그 생성 중 오류 발생:", error);
          alert("태그를 생성하는 데 문제가 발생했습니다. 다시 시도해주세요.");
        });
    });
  });

  // GPT API 호출 함수
  function fetchGPTTags(url, title) {
    const apiKey = config.OPENAI_API_KEY;
    const apiUrl = "https://api.openai.com/v1/chat/completions";

 const prompt = `chose one category and Generate 5 Korean tags for the given website based on its URL and title. 
    URL: ${url}, Title: ${title}
    The "tag1" must be one of the following category: "교육, 비즈니스, 엔터테인먼트, 건강 및 피트니스, 기술 및 개발, 금융 및 투자, 쇼핑, 여행, 예술 및 창작, 기타".
    Output format: tag1, tag2, tag3, tag4, tag5, tag6
    `;

    return fetch(apiUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${apiKey}`,
      },
      body: JSON.stringify({
        model: "gpt-3.5-turbo",
        messages: [
          {
            role: "system",
            content: "You are an assistant that generates tags for websites.",
          },
          { role: "user", content: prompt },
        ],
        max_tokens: 50,
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        if (
          data.choices &&
          data.choices.length > 0 &&
          data.choices[0].message.content
        ) {
          const tags = data.choices[0].message.content
            .trim()
            .split(",")
            .map((tag) => tag.trim())
            .slice(0, 6); // 최대 6개의 태그 반환
          return tags;
        } else {
          throw new Error("GPT 응답에서 태그를 생성할 수 없습니다.");
        }
      });
  }

  // 태그 UI 추가
  function addTagsToUI(tags) {
    tagContainer.innerHTML = ""; // 기존 태그 초기화
    const colors = [
      "#FF6666",
      "#FFB266",
      "#FFFF66",
      "#B2FF66",
      "#66FF66",
      "#66FFB2",
    ]; // 버튼 색상 배열
    let colorIndex = 0;

    tags.forEach((tag) => {
      const tagElement = document.createElement("button");
      tagElement.className = "tag";
      tagElement.textContent = tag;
      tagElement.style.backgroundColor = "#E8EDF2";
      tagElement.style.border = "none";
      tagElement.style.color = "#0D141C";
      tagElement.style.padding = "5px 10px";
      tagElement.style.margin = "3px";
      tagElement.style.borderRadius = "5px";
      tagElement.style.cursor = "pointer";



      tagElement.addEventListener("click", function () {
        filterBookmarksByTag(tag); // 태그 클릭 시 해당 북마크 필터링
      });

      tagElement.addEventListener("mouseover", () => {
        tagElement.style.backgroundColor = "#29DE2C"; // hover 시 색상
        tagElement.style.color = "#ffffff";         // hover 시 글자 색
        tagElement.style.transform = "scale(1.05)"; // hover 시 크기
      });
          

      tagElement.addEventListener("mouseout", () => {
        tagElement.style.backgroundColor = "#E8EDF2"; // 원래 색상으로 복귀
        tagElement.style.color = "#0D141C";          // 원래 글자 색으로 복귀
        tagElement.style.transform = "scale(1)";     // 크기 복귀
      });

      tagContainer.appendChild(tagElement);
      colorIndex = (colorIndex + 1) % colors.length;
    });
  }

  // 북마크 UI 추가
  function addBookmarkToUI(bookmark) {
    const listItem = document.createElement("div");
    listItem.className = "bookmark-item";
    listItem.innerHTML = `
    <a href="${bookmark.url}" target="_blank" style="width:120px;margin:10px;flex-direction:column;align-items:center;justify-content:center;display:flex">
    <img class="bookmark-img" src="${bookmark.imageUrl}" alt="${bookmark.title}" style="cursor:pointer"/>
    <button class="bookmark-title">${bookmark.title}</button>
    `;
    bookmarkList.appendChild(listItem);
  }

  // 북마크 필터링
  function filterBookmarksByTag(tag) {
    const filteredBookmarks = bookmarkCache.filter((bookmark) =>
      bookmark.tags.includes(tag)
    );
    displayBookmarks(filteredBookmarks);
  }

  // 북마크 목록 표시
  function displayBookmarks(bookmarks) {
    bookmarkList.innerHTML = ""; // 기존 목록 초기화
    bookmarks.forEach(addBookmarkToUI);
  }

  // 초기화 함수
  function initializeData() {
    console.log(userEmail, "백엔드에서 가져올 userEmail");//test
    getAllBookmarksFromBackend(userEmail)
      .then((bookmarks) => {
        console.log("초기 데이터:", bookmarks);

        // 캐시에 데이터 저장
        bookmarkCache = bookmarks;
        tagCache = Array.from(
          new Set(bookmarks.flatMap((bookmark) => bookmark.tags))
        );

        let showTags = [];
        bookmarks.forEach((bookmark) => {
          showTags.push(bookmark.tags[0]);
        })
        console.log("first tags", showTags);//test

        // UI 업데이트
        displayBookmarks(bookmarks);
        addTagsToUI(showTags);
        //addTagsToUI(tagCache);
      })
      .catch((error) => {
        console.error("초기화 중 오류 발생:", error);
      });
  }

  //initializeData();
});
