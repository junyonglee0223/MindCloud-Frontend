import { openEditPage } from "./util_page.js";
const userEmail = "test1@gmail.com";
document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.getElementById('searchInput');
    const searchBtn = document.getElementById('searchBtn');
    //const goBackBtn = document.getElementById('goBackBtn');
    const searchResults = document.getElementById('searchResults');
  

    // 검색 버튼 클릭 이벤트
    searchBtn.addEventListener('click', function () {
        const query = searchInput.value.trim();
        if (!query) {
          alert('검색어를 입력하세요.');
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

    
    // 북마크 리스트 아이템 생성 함수
    function createBookmarkListItem(bookmark) {
        // 기본 썸네일 이미지
        const defaultThumbnail = "thumbnail-div-box-1-img0.png"; // 기본 이미지 경로
        // 썸네일 URL이 제공되었는지 확인
        const thumbnailUrl = bookmark.thumbnailUrl ? bookmark.thumbnailUrl : defaultThumbnail;

        // 제목 설정 (title이 없으면 bookmarkName 사용)
        const title = bookmark.title || bookmark.bookmarkName || "제목 없음";


        // 썸네일 박스 생성
        const thumbnailBox = document.createElement("div");
        thumbnailBox.classList.add("thumbnail-div-box-1");

        // 썸네일 HTML 구조
        thumbnailBox.innerHTML = `
            <img
            class="thumbnail-div-box-1-img"
            src="${thumbnailUrl}" 
            alt="${title} 이미지"
            />
            <div class="thumbnail-div-box-1-inbox">
            <div class="thumbnail-div-box-1-inbox-title">
                <div>${title}</div>
            </div>
            <div class="thumbnail-div-box-1-inbox-url">
                <div>${bookmark.url}</div>
            </div>
            </div>
            <div class="thumbnail-div-box-1-tagbox">
            ${bookmark.tags
                .map(
                (tag) => `
                <div class="thumbnail-div-box-1-tagbox-tag">
                <div>${tag}</div>
                </div>`
                )
                .join("")}
            </div>
        `;

        // 컨텍스트 메뉴 추가
        addContextMenuForBookmark(thumbnailBox, bookmark, userEmail);
        return thumbnailBox;
      }


    // 검색 결과 표시 함수
    function displaySearchResults(tagResults, bookmarkResults, query) {
        searchResults.innerHTML = ''; // 기존 결과 초기화
    
        if (tagResults.length === 0 && bookmarkResults.length === 0) {
          searchResults.innerHTML = `<div>검색어 "${query}"에 대한 결과가 없습니다.</div>`;
          return;
        }
    
        // 태그 검색 결과 출력
        if (tagResults.length > 0) {
          const tagHeader = document.createElement('h3');
          tagHeader.textContent = '태그로 검색된 결과';
          searchResults.appendChild(tagHeader);
    
          tagResults.forEach(function (bookmark) {
            // createBookmarkListItem 함수 사용
            const bookmarkItem = createBookmarkListItem(bookmark);
            searchResults.appendChild(bookmarkItem);
          });
        }
    
        // 북마크 이름 검색 결과 출력
        if (bookmarkResults.length > 0) {
          const bookmarkHeader = document.createElement('h3');
          bookmarkHeader.textContent = '북마크 이름으로 검색된 결과';
          searchResults.appendChild(bookmarkHeader);
    
          bookmarkResults.forEach(function (bookmark) {
            // createBookmarkListItem 함수 사용
            const bookmarkItem = createBookmarkListItem(bookmark);
            searchResults.appendChild(bookmarkItem);
          });
        }
      }
  
  
    
    


    // 모든 북마크 출력 함수
    function displayAllBookmarks() {
        getAllBookmarksFromBackend(userEmail) // backend.js의 함수 호출
        .then((bookmarks) => {
            searchResults.innerHTML = ''; // 기존 결과 초기화

            if (bookmarks.length === 0) {
            searchResults.innerHTML = 
            '<div class="no-results">저장된 북마크가 없습니다.</div>';
            return;
            }
            // 모든 북마크 출력
            bookmarks.forEach((bookmark) => {
            const listItem = createBookmarkListItem(bookmark);
            searchResults.appendChild(listItem);
            });
        })
        .catch((error) => {
            console.error('북마크 불러오기 중 오류 발생:', error);
            alert('북마크를 불러오는 데 실패했습니다.');
        });
        }
      
    // 페이지 로드 시 기본 북마크 출력
    displayAllBookmarks();
    window.displayAllBookmarks = displayAllBookmarks;

});
  