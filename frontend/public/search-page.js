document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("searchInput");
    const searchBtn = document.getElementById("searchBtn");
    const goBackBtn = document.getElementById("goBackBtn");
    const searchResults = document.getElementById("searchResults");
  
    // 검색 버튼 클릭 이벤트
    searchBtn.addEventListener("click", function () {
      const query = searchInput.value.trim();
  
      if (!query) {
        alert("검색어를 입력하세요.");
        displayAllBookmarks(); // 빈 검색어일 경우 모든 북마크 표시
        return;
      }
  
      searchBookmarks(query)
        .then((results) => {
          displaySearchResults(results, query);
        })
        .catch((error) => {
          console.error("검색 중 오류 발생:", error);
          alert("검색 중 문제가 발생했습니다. 다시 시도해주세요.");
        });
    });
  
    // 뒤로가기 버튼 클릭 이벤트
    goBackBtn.addEventListener("click", function () {
      window.location.href = "popup.html"; // popup.html로 이동
    });
  
    // 검색된 북마크 표시 함수
    function displaySearchResults(results, query) {
      searchResults.innerHTML = ""; // 기존 결과 초기화
  
      if (results.length === 0) {
        searchResults.innerHTML = `<li>검색어 "${query}"에 대한 결과가 없습니다.</li>`;
        return;
      }
  
      // 검색 결과 표시
      results.forEach((bookmark) => {
        const listItem = document.createElement("li");
        listItem.innerHTML = `<a href="${bookmark.url}" target="_blank">${bookmark.title}</a>
          <p>Tags: ${bookmark.tags.join(", ")}</p>`;
        searchResults.appendChild(listItem);
      });
    }
  
    // 모든 북마크 표시 함수
    function displayAllBookmarks() {
      getAllBookmarks()
        .then((bookmarks) => {
          searchResults.innerHTML = ""; // 기존 결과 초기화
  
          if (bookmarks.length === 0) {
            searchResults.innerHTML = "<li>저장된 북마크가 없습니다.</li>";
            return;
          }
  
          bookmarks.forEach((bookmark) => {
            const listItem = document.createElement("li");
            listItem.innerHTML = `<a href="${bookmark.url}" target="_blank">${bookmark.title}</a>
              <p>Tags: ${bookmark.tags.join(", ")}</p>`;
            searchResults.appendChild(listItem);
          });
        })
        .catch((error) => {
          console.error("북마크 불러오기 중 오류 발생:", error);
          alert("북마크를 불러오는 데 실패했습니다.");
        });
    }
  
    // 북마크 검색 함수
    function searchBookmarks(query) {
      return new Promise((resolve, reject) => {
        chrome.storage.sync.get("bookmarks", ({ bookmarks }) => {
          if (chrome.runtime.lastError) {
            reject(chrome.runtime.lastError);
            return;
          }
  
          const results = (bookmarks || []).filter((bookmark) => {
            return (
              bookmark.title.toLowerCase().includes(query.toLowerCase()) ||
              bookmark.tags.some((tag) =>
                tag.toLowerCase().includes(query.toLowerCase())
              )
            );
          });
  
          resolve(results);
        });
      });
    }
  
    // 모든 북마크 가져오기 함수
    function getAllBookmarks() {
      return new Promise((resolve, reject) => {
        chrome.storage.sync.get("bookmarks", ({ bookmarks }) => {
          if (chrome.runtime.lastError) {
            reject(chrome.runtime.lastError);
            return;
          }
          resolve(bookmarks || []);
        });
      });
    }
  
    // 페이지 로드 시 모든 북마크 표시
    displayAllBookmarks();
  });
  