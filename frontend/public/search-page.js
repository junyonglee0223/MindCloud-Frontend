import { openEditPage } from "./util_page.js";
//const userEmail = "test1@gmail.com";
let query = "";

const recBookmarks = [
  {
    email: "test1@gmail.com",       // 사용자의 이메일
    imageUrl: "", // 이미지 URL
    tags: ["", "", ""],     // 북마크의 태그
    title: "",               // 북마크 제목
    url: "",         // 북마크 URL
    userName: "test1"            // 사용자의 이름
  },
  {
    email: "test1@gmail.com",       // 사용자의 이메일
    imageUrl: "", // 이미지 URL
    tags: ["", "", ""],     // 북마크의 태그
    title: "",               // 북마크 제목
    url: "",         // 북마크 URL
    userName: "test1"            // 사용자의 이름
  },
  {
    email: "test1@gmail.com",       // 사용자의 이메일
    imageUrl: "", // 이미지 URL
    tags: ["", "", ""],     // 북마크의 태그
    title: "",               // 북마크 제목
    url: "",         // 북마크 URL
    userName: "test1"            // 사용자의 이름
  },
];

document.addEventListener('DOMContentLoaded', function () {
    let userEmail;

    const searchInput = document.getElementById('searchInput');
    const searchBtn = document.getElementById('searchBtn');
    //const goBackBtn = document.getElementById('goBackBtn');
    const searchResults = document.getElementById('searchResults');
    const searchSetting = document.getElementById('searchSetting');
    const recResults = document.getElementById('recResults');
    const recText = document.getElementById('recText');



    let loadedbookmarks = []; // 전역 변수로 bookmarks 선언

      // 검색 버튼 클릭 이벤트
    searchBtn.addEventListener('click', handleSearch);
    searchInput.addEventListener('keydown', function (event) {
      if (event.key === 'Enter') {
          handleSearch();
      }
    });

    
async function fetchRecommendedSites(query) {
  
  const API_KEY = config.OPENAI_API_KEY; // OpenAI API Key
  const apiUrl = 'https://api.openai.com/v1/chat/completions';  // chat/completions 엔드포인트 사용
  
  const prompt = `${query} 이 사이트랑 비슷한 사이트 3개를 추천해줘. Provide title and url.
  양식은 다음과 같아. title: "", url: "",
  `;
  
  const response = await fetch(apiUrl, {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${API_KEY}`
      },
      body: JSON.stringify({
          model: 'gpt-3.5-turbo',  // 적절한 모델 선택
          messages: [
              {
                  role: 'system',
                  content: "You are an assistant that recommends similar websites based on the given search query."
              },
              {
                  role: 'user',
                  content: prompt
              }
          ],
          max_tokens: 150,  // 추천 사이트와 제목, URL을 포함할 충분한 토큰 수
          temperature: 0.7
      })
  });

  const data = await response.json();
  console.log("추천 사이트",data)

   
  // API 응답에서 추천된 사이트 정보 추출
  const recommendedSitesText = data.choices[0].message.content.trim();
  console.log("Recommended Sites Text:", recommendedSitesText); // API에서 반환된 데이터 확인


  // '\n'을 기준으로 각 사이트 데이터를 나누고, 'title'과 'url'을 추출
  const recommendedSites = recommendedSitesText.split("\n").map(siteInfo => {
    // 정규 표현식 수정: '1. title: 제목, url: URL' 형식에 맞게 수정
    const match = siteInfo.match(/title:\s(.*?),\surl:\s(https?:\/\/[^\s]+)/);
    console.log("Matching Result:", match); // 정규 표현식 매칭 확인
    return match ? { title: match[1], url: match[2] } : null;
  }).filter(site => site !== null);

  console.log("추천 사이트",recommendedSites)


  // recBookmarks 배열에 title과 url을 추가
  const recBookmarks = recommendedSites.map((site, index) => ({
    email: "test1@gmail.com",       // 사용자의 이메일
    imageUrl: "",                   // 이미지 URL
    tags: ["", "", ""],             // 북마크의 태그
    title: site.title,              // 북마크 제목
    url: site.url,                  // 북마크 URL
    userName: "test1"               // 사용자의 이름
  }));
  console.log("추천 사이트",recBookmarks[0])



  // recommendedSites.forEach(async site => {
  //   const recBookmarksItem = document.createElement('div');
  //   let thumbnailUrl = await fetchThumbnailUrl(site.url);
  //   recBookmarksItem.className = 'rec-bookmarks-item';
  //   recBookmarksItem.style.cssText = `
  //     padding: 0px 0px 12px 0px;
  //     display: flex;
  //     flex-direction: column;
  //     gap: 12px;
  //     align-items: flex-start;
  //     justify-content: flex-start;
  //     align-self: stretch;
  //     flex-shrink: 0;
  //     width: 222px;
  //     position: relative;
  //   `;

  //   recBookmarksItem.innerHTML = `
  //       <a href="${site.url}" target="_blank">
  //       <img
  //       href="${site.url}"
  //       class="thumbnail-div-box-1-img"
  //       src="${thumbnailUrl}" 
  //       alt="${site.title} 이미지"
  //       />
  //       <div 
  //       class="thumbnail-div-box-1-inbox"
  //       stlye=" justify-content: center;"
  //       >
  //       <div class="thumbnail-div-box-1-inbox-title">
  //           <div>${site.title}</div>
  //       </div>
  //       <div 
  //       class="thumbnail-div-box-1-inbox-url">
  //           <div
  //           >${site.url}</div>
  //       </div>
  //       </div>
  //       </a>
  //       <div class="thumbnail-div-box-1-tagbox"></div>
  //   `;

    
  //   // 비동기적으로 fetch가 끝난 후에 DOM에 추가
  //   recResults.appendChild(recBookmarksItem);
  // });
// 추천 사이트 배열을 비동기적으로 처리

recText.style.cssText = `
width: 222px;
display: flex;
font-size: 18px; /* 폰트 크기 */
font-weight: bold; /* 볼드체 */
display: flex; /* Flexbox */
align-items: center; /* Flexbox 중앙 정렬 (수직) */
justify-content: center; /* Flexbox 중앙 정렬 (수평) */
margin-bottom:21px;
">
`;
recText.innerHTML = `
AI 추천 사이트
`
;

for (let i = 0; i < 3; i++) {
  
  const site = recommendedSites[i];

  const recBookmarksItem = document.createElement('div');
  // recBookmarksItem.innerHTML="";
  
  // 비동기적으로 썸네일 URL을 가져옴
  let thumbnailUrl = await fetchThumbnailUrl(site.url);
  // let thumbnailUrl="";
  // 아이템의 스타일을 설정
  recBookmarksItem.className = 'rec-bookmarks-item';
  recBookmarksItem.style.cssText = `
      padding: 0px 0px 12px 0px;
      display: flex;
      flex-direction: column;
      gap: 12px;
      align-items: flex-start;
      justify-content: flex-start;
      align-self: stretch;
      flex-shrink: 0;
      width: 222px;
      position: relative;
      font-size: 18px; /* 폰트 크기 */
      font-weight: bold; /* 볼드체 */
      display: flex; /* Flexbox */
      align-items: center; /* Flexbox 중앙 정렬 (수직) */
      justify-content: center; /* Flexbox 중앙 정렬 (수평) */
      margin-bottom:20px;
      margin-left:20px;

      ">
  `;

  // HTML 내용 설정
  recBookmarksItem.innerHTML = `
      <a href="${site.url}" target="_blank">
          <img
              class="thumbnail-div-box-1-img"
              src="${thumbnailUrl}" 
              alt="${site.title} 이미지"
          />
          <div class="thumbnail-div-box-1-inbox" style="justify-content: center;">
              <div class="thumbnail-div-box-1-inbox-title">
                  <div>${site.title}</div>
              </div>
              <div class="thumbnail-div-box-1-inbox-url">
                  <div>${site.url}</div>
              </div>
          </div>
      </a>
      <div class="thumbnail-div-box-1-tagbox"></div>
  `;
  
  // 비동기적으로 fetch가 끝난 후에 DOM에 추가
  recResults.appendChild(recBookmarksItem);

  console.log("한번 돔",i)
}


  return recBookmarks;
}



    function handleSearch(){
      query = searchInput.value.trim();
        if (!query) {
          //alert('검색어를 입력하세요.');
          displayAllBookmarks();      //빈 문자 입력 시 
          return;
        }
  
        fetchSearchResults(query, userEmail)
          .then((data) => {
            // 검색 결과 출력
            console.log(data);
            console.log(data.tagResults);//test
            console.log(data.bookmarkResults);//test
            displaySearchResults(data.tagResults, data.bookmarkResults, query);
          })
          .catch((error) => {
            console.error('검색 중 오류 발생:', error);
            alert('검색 중 문제가 발생했습니다. 다시 시도해주세요.');
        });

    }

    // 검색 버튼 클릭 이벤트
    searchBtn.addEventListener('click', function () {
        
      });

    // 검색 버튼 클릭 이벤트
    searchBtn.addEventListener('click', function () {
        
      });

    // 검색 버튼 클릭 이벤트
    searchBtn.addEventListener('click', function () {
        
      });


    function addContextMenuForBookmark(listItem, bookmark, userEmail) {
        listItem.addEventListener('contextmenu', function (event) {
          event.preventDefault(); // 기본 컨텍스트 메뉴 방지
      
          // 기존 메뉴가 있으면 제거
          const existingMenu = document.getElementById('context-menu');
          if (existingMenu) existingMenu.remove();
      
          // 커스텀 컨텍스트 메뉴 생성
          const menu = document.createElement('div');
          menu.id = 'context-menu';
          menu.style.position = 'absolute';
          menu.style.top = `${event.pageY}px`;
          menu.style.left = `${event.pageX}px`;
          menu.style.backgroundColor = '#fff';
          menu.style.border = '1px solid #ccc';
          menu.style.padding = '10px';
          menu.style.boxShadow = '0px 2px 4px rgba(0, 0, 0, 0.1)';
          menu.style.zIndex = '1000';
      
          // 삭제 메뉴 추가
          const deleteOption = document.createElement('div');
          deleteOption.textContent = '삭제';
          deleteOption.style.cursor = 'pointer';
          deleteOption.style.padding = '5px';
          deleteOption.style.color = 'red';
      
          deleteOption.addEventListener('click', function () {
            menu.remove(); // 컨텍스트 메뉴 제거
            deleteBookmark(bookmark.title, userEmail); // 삭제 동작 실행
          });
      
          menu.appendChild(deleteOption);
  
          // 수정 메뉴 추가
          const editOption = document.createElement('div');
          editOption.textContent = '수정';
          editOption.style.cursor = 'pointer';
          editOption.style.padding = '5px';
          editOption.style.color = 'blue';
  
          editOption.addEventListener('click', function () {
              menu.remove(); // 컨텍스트 메뉴 제거
              console.log(openEditPage);//test
              console.log("수정하기 클릭한 객체 bookmark data", bookmark);//test




              openEditPage(bookmark, "edit"); // 수정 팝업 열기
          });
  
          menu.appendChild(editOption);
      
          // 컨텍스트 메뉴 닫기
          document.addEventListener('click', function closeMenu() {
            menu.remove();
            document.removeEventListener('click', closeMenu);
          });
      
          document.body.appendChild(menu);
        });
      }
      
    // 삭제 동작에 확인 팝업 추가
    function deleteBookmark(title, userEmail) {
        if (!confirm(`정말로 "${title}" 북마크를 삭제하시겠습니까?`)) {
          return; // 사용자가 취소하면 삭제하지 않음
        }
  
        // 삭제 실행
        deleteBookmarkFromBackend(title, userEmail)
          .then(() => {
            displayAllBookmarks(); // 삭제 후 북마크 목록 갱신
          })
          .catch((error) => {
            console.error('북마크 삭제 중 오류 발생:', error);
            alert('북마크 삭제에 실패했습니다.');
          });
      }

    
  // 랜덤 색상 생성 함수
function getRandomColor() {
  const colors = ["#FF5733", "#33FF57", "#3357FF", "#F3FF33", "#FF33F3", "#33F3FF"];
  return colors[Math.floor(Math.random() * colors.length)];
}

// async function fetchThumbnailUrl(url) {
//   try {
//       console.log(`Fetching thumbnail for URL: ${url}`); // 디버깅 로그 추가
//       const response = await fetch(url);
      
//       if (!response.ok) {
//           throw new Error(`HTTP error! status: ${response.status}`);
//       }

//       const html = await response.text();
//       const doc = new DOMParser().parseFromString(html, 'text/html');
//       const metaTags = doc.getElementsByTagName('meta');
      
//       for (let meta of metaTags) {
//           if (meta.getAttribute('property') === 'og:image' || meta.getAttribute('name') === 'twitter:image') {
//               const thumbnailUrl = meta.getAttribute('content');
//               console.log(`Found thumbnail URL: ${thumbnailUrl}`); // 디버깅 로그 추가
//               return thumbnailUrl;
//           }
//       }
//       console.warn('No thumbnail URL found in meta tags');
//       return "thumbnail-div-box-1-img0.png"; // 기본 이미지 경로
//   } catch (error) {
//       console.error('썸네일 URL 추출 중 오류 발생:', error);
//       return "thumbnail-div-box-1-img0.png"; // 기본 이미지 경로
//   }
// }

// Set to keep track of processed URLs
const processedUrls = new Set();

async function fetchThumbnailUrl(url) {
  try {
    // 이미 처리된 URL 확인
    if (processedUrls.has(url)) {
      console.log(`이미 처리된 URL: ${url}`);
      return; // 이미 처리된 URL이면 종료
    }
    
    // URL 처리
    processedUrls.add(url);

    const response = await fetch(url);
    
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const html = await response.text();
    const doc = new DOMParser().parseFromString(html, 'text/html');
    
    const metaTags = doc.getElementsByTagName('meta');
    
    for (let meta of metaTags) {
      const property = meta.getAttribute('property');
      const name = meta.getAttribute('name');
      
      if (property === 'og:image' || name === 'twitter:image') {
        const thumbnailUrl = meta.getAttribute('content');
        
        if (thumbnailUrl) {
          console.log(`Found thumbnail URL: ${thumbnailUrl}`);
          return thumbnailUrl;
        }
      }
    }

    // 썸네일을 찾을 수 없으면 기본 이미지 사용
    console.warn('No thumbnail URL found in meta tags.');
    return "/path/to/default-thumbnail.png";  // 경로를 절대경로로 설정해야 할 수도 있습니다.

  } catch (error) {
    console.error('Error fetching thumbnail URL:', error);
    return "/path/to/default-thumbnail.png";  // 경로를 절대경로로 설정해야 할 수도 있습니다.
  }
}



// 북마크 리스트 아이템 생성 함수 수정
async function createBookmarkListItem(bookmark) {
    const defaultThumbnail = "myprofile0.png"; // 기본 이미지 경로
    //let thumbnailUrl = bookmark.thumbnailUrl ? bookmark.thumbnailUrl : await fetchThumbnailUrl(bookmark.url); 

    //let thumbnailUrl = bookmark.imageUrl ? bookmark.imageUrl : defaultThumbnail;
    const validateImage = async (url) =>{
      return new Promise((resolve) => {
        const img = new Image();
        img.onload = () => resolve(url); // 유효한 이미지
        img.onerror = () => resolve(defaultThumbnail); // 기본 이미지로 대체
        img.src = url;
    });
    };
    let thumbnailUrl = await validateImage(bookmark.imageUrl || defaultThumbnail);
     
    const title = bookmark.title || bookmark.bookmarkName || "제목 없음";

    const thumbnailBox = document.createElement("div");
    thumbnailBox.classList.add("thumbnail-div-box-1");

    thumbnailBox.innerHTML = `
        <a href="${bookmark.url}" target="_blank">
        <img
        href="${bookmark.url}"
        class="thumbnail-div-box-1-img"
        src="${thumbnailUrl}" 
        alt="${title} 이미지"
        />
        <div 
        class="thumbnail-div-box-1-inbox"
        stlye=" justify-content: center;"
        >
        <div class="thumbnail-div-box-1-inbox-title">
            <div>${title}</div>
        </div>
        <div 
        class="thumbnail-div-box-1-inbox-url">
            <div
            style="overflow:hidden;width:222px"
            >${bookmark.url}
            </div>
        </div>
        </div>
        </a>
        <div class="thumbnail-div-box-1-tagbox"></div>
    `;

    const tagBox = thumbnailBox.querySelector(".thumbnail-div-box-1-tagbox");

    // 태그를 버튼으로 생성
    bookmark.tags.forEach((tag) => {
        const tagButton = document.createElement("button");
        tagButton.textContent = tag;
        tagButton.style.backgroundColor = "#E8EDF2";
        tagButton.style.border = "none";
        tagButton.style.color = "#0D141C";
        tagButton.style.padding = "5px 10px";
        tagButton.style.margin = "3px";
        tagButton.style.borderRadius = "5px";
        tagButton.style.cursor = "pointer";

        // 태그 버튼 클릭 이벤트 
        tagButton.addEventListener("click", function () {
          const filteredBookmarks = loadedbookmarks.filter(bookmark =>
              bookmark.tags.includes(tag)
              
          );
          displayFilteredBookmarks(filteredBookmarks, tag);
          console.log("필터링된 북마크:", filteredBookmarks);
          // 클릭된 버튼만 색 변경
          tagButton.style.backgroundColor = "#29DE2C";
          tagButton.style.color = "#ffffff";
      
          // 다른 버튼 초기화
          const otherButtons = tagBox.querySelectorAll("button");
          otherButtons.forEach((button) => {
              if (button !== tagButton) {
                  button.style.backgroundColor = "#E8EDF2"; // 기본 색상 복구
                  button.style.color = "#0D141C";          // 기본 글자 색 복구
              }
          });
      });
      

                // Hover 효과 추가
        if (!tagButton.classList.contains('selected')) { 
        tagButton.addEventListener("mouseover", () => {
          tagButton.style.backgroundColor = "#29DE2C"; // hover 시 색상
          tagButton.style.color = "#ffffff";         // hover 시 글자 색
          tagButton.style.transform = "scale(1.05)"; // hover 시 크기
        });
            

        tagButton.addEventListener("mouseout", () => {
          tagButton.style.backgroundColor = "#E8EDF2"; // 원래 색상으로 복귀
          tagButton.style.color = "#0D141C";          // 원래 글자 색으로 복귀
          tagButton.style.transform = "scale(1)";     // 크기 복귀
        });
      }
        
        // 선택된 태그에 'selected' 클래스 추가
        tagButton.addEventListener('click', () => {
          // 클릭된 버튼에 'selected' 클래스 추가
          tagButton.classList.add('selected');
          // 선택된 버튼의 색상 고정
          tagButton.style.backgroundColor = "#29DE2C";
          tagButton.style.color = "#ffffff";
          tagButton.style.transform = "scale(1)"; // 크기 고정
        });
        tagBox.appendChild(tagButton);
    });


    addContextMenuForBookmark(thumbnailBox, bookmark, userEmail);
    return thumbnailBox;
}


// 필터링된 북마크 표시 함수
async function displayFilteredBookmarks(filteredBookmarks, tag) {

  searchResults.innerHTML = '';
  searchSetting.style.flexDirection="row";
  searchSetting.style.display="row";

  searchbox3.style.flexDirection="column";

//   searchText.innerHTML = `
//   <div style="
//   font-size: 18px; /* 폰트 크기 */
//   font-weight: bold; /* 볼드체 */
//   display: flex; /* Flexbox */
//   align-items: center; /* Flexbox 중앙 정렬 (수직) */
//   justify-content: center; /* Flexbox 중앙 정렬 (수평) */
//   margin-bottom:20px;>
//     태그로 검색된 결과 "${tag}"
//   </div>
// `;

  if (filteredBookmarks.length === 0) {
    searchSetting.innerHTML += `<div>해당 태그의 북마크가 없습니다.</div>`;
      console.log('해당 태그의 북마크가 없습니다:', tag); // 디버깅 로그 추가
      return;
  }

  for (const bookmark of filteredBookmarks) {
      const listItem = await createBookmarkListItem(bookmark); // 비동기 함수 호출
      if (listItem instanceof Node) {
        searchSetting.appendChild(listItem);
      } else {
          console.error("Invalid Node returned from createBookmarkListItem:", listItem);
      }
  }

  // 태그 버튼 색상 변경 
  const tagButtons = document.querySelectorAll('.thumbnail-div-box-1-tagbox button'); 
  tagButtons.forEach(button => { 
    if (button.textContent === tag) 
    { button.style.backgroundColor = '#29DE2C';
       // 원하는 배경색으로 변경 
       button.style.color = '#ffffff'; 
       // 원하는 텍스트 색상으로 변경 
       } else 
       { button.style.backgroundColor =  "#E8EDF2"; 
        // 기본 색상으로 변경 
        button.style.color = "#0D141C"; 
      } 
      });
}


    // 검색 결과 표시 함수
    async function displaySearchResults(tagResults, bookmarkResults, query) {

      const resultsContainer = document.getElementById('results');

      
      searchSetting.innerHTML = ''; // 기존 결과 초기화
      searchResults.innerHTML = ''; // 기존 결과 초기화
      searchText.innerHTML='';

      if (tagResults.length === 0 && bookmarkResults.length === 0) {
        searchSetting.innerHTML = `<div>검색어 "${query}"에 대한 결과가 없습니다.</div>`;
          return;
      }

      
  
      // 북마크 이름 검색 결과 출력
      if (bookmarkResults.length > 0) {
          const bookmarkHeader = document.createElement('h3');
          bookmarkHeader.style.marginTop="10px";
          bookmarkHeader.textContent = '북마크 이름으로 검색된 결과';
          bookmarkHeader.style.fontSize= "18px"; /* 폰트 크기 */
          bookmarkHeader.style.fontWeight= "bold"; /* 볼드체 */
          bookmarkHeader.style.marginBottom="35px";
          searchText.appendChild(bookmarkHeader);
  
          for (const bookmark of bookmarkResults) {
              const bookmarkItem = await createBookmarkListItem(bookmark); // 비동기 함수 호출
              if (bookmarkItem instanceof Node) {
                searchText.appendChild(bookmarkItem);
              } else {
                  console.error("Invalid Node returned from createBookmarkListItem:", bookmarkItem);
              }
          }

          // const recBookmarksItem = await createBookmarkListItem(recBookmarks[0]); // 비동기 함수 호출
          // recResults.appendChild(recBookmarksItem);


          
      }

      // 태그 검색 결과 출력
      if (tagResults.length > 0) {
        const tagHeader = document.createElement('h3');
        tagHeader.style.marginTop="50px";
        tagHeader.textContent = '태그로 검색된 결과';
        tagHeader.style.fontSize= "18px"; /* 폰트 크기 */
        tagHeader.style.fontWeight= "bold"; /* 볼드체 */
        tagHeader.style.marginBottom="35px";

        searchText.appendChild(tagHeader);

        for (const bookmark of tagResults) {
            const bookmarkItem = await createBookmarkListItem(bookmark); // 비동기 함수 호출
            if (bookmarkItem instanceof Node) {
              searchSetting.appendChild(bookmarkItem);
            } else {
                console.error("Invalid Node returned from createBookmarkListItem:", bookmarkItem);
            }
        }
    }


      // 검색된 태그의 색 변경
      const tagButtons = document.querySelectorAll('.thumbnail-div-box-1-tagbox button'); 
      tagButtons.forEach(button => { 
          if (button.textContent.includes(query)) { 
              button.style.backgroundColor = '#29DE2C';  // 원하는 배경색으로 변경 
              button.style.color = '#ffffff';           // 원하는 텍스트 색상으로 변경 
          } else { 
              button.style.backgroundColor = "#E8EDF2"; // 기본 색상으로 변경 
              button.style.color = "#0D141C";           // 기본 텍스트 색상으로 변경 
          }
      });

      await fetchRecommendedSites(query);

        // // 추천 사이트 표시
        // const recommendationsHTML = recommendedSites.map(site => {
        //   const [title, url] = site.split('-'); // 'Title - URL' 형식으로 분리
        //   return `<div><a href="${url.trim()}" target="_blank">${title.trim()}</a></div>`;
        // }).join('');

        // resultsContainer.innerHTML += `
        //   <div class="recommendations">
        //       <h3>Recommended Sites</h3>
        //       ${recommendationsHTML}
        //   </div>
        // `;
  }
  
  
  document.querySelector('.title-box').addEventListener('click', () => {
    location.reload(); // 새로고침
  });
  
    


    // 모든 북마크 출력 함수
    async function displayAllBookmarks() {
      try {
          const bookmarks = await getAllBookmarksFromBackend(userEmail); // backend.js의 함수 호출
          searchResults.innerHTML = ''; // 기존 결과 초기화
  
          loadedbookmarks = bookmarks;
          console.log("북마크들 형식",bookmarks)

  
          if (bookmarks.length === 0) {
              searchResults.innerHTML = 
              '<div class="no-results">저장된 북마크가 없습니다.</div>';
              return;
          }
          
          // 모든 북마크 출력
          for (const bookmark of bookmarks) {
              const listItem = await createBookmarkListItem(bookmark); // 비동기 함수 호출
              if (listItem instanceof Node) {
                  searchResults.appendChild(listItem);
                  console.log("북마크 형식",bookmark)
              } else {
                  console.error("Invalid Node returned from createBookmarkListItem:", listItem);
              }
          }
      } catch (error) {
          console.error('북마크 불러오기 중 오류 발생:', error);
          alert('북마크를 불러오는 데 실패했습니다.');
      }
  }
  
  chrome.storage.sync.get("userData", ({ userData }) => {
    if (userData && userData.email) {
      userEmail = userData.email; // Retrieve the email dynamically
      console.log("User email:", userEmail);
        // 페이지 로드 시 기본 북마크 출력
        displayAllBookmarks();
        window.displayAllBookmarks = displayAllBookmarks;

    } else {
      console.error("User email not found in chrome.storage.sync");
    }
  });
});
  