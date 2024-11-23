//const userEmail = "test1@gmail.com";
// 팝업 오버레이 관리 로직
document.addEventListener('DOMContentLoaded', function () {
    const popupOverlay = document.getElementById('popupOverlay');
    const bookmarkNameInput = document.getElementById('bookmarkName');
    const bookmarkUrlInput = document.getElementById('bookmarkUrl');
    const tagList = document.getElementById('tagList');
    const addTagInput = document.getElementById('addTagInput');
    const addTagBtn = document.getElementById('addTagBtn');
    const saveBtn = document.getElementById('saveBtn');
    const cancelBtn = document.getElementById('cancelBtn');
  
    let currentBookmark = null; // 현재 편집 중인 북마크 데이터
    let prevBookmarkName = "";
    let mode = "save";
    console.log('Popup overlay element:', popupOverlay);
    console.log("Initial popupOverlay classList:", popupOverlay.classList);
    //test!!!!!!!!!!!!!!!!!!!!!!!!!

    popupOverlay.classList.add('hidden'); // 항상 숨겨진 상태로 시작
    
    // 팝업 열기 함수
    window.openEditPopup = function (bookmark, popupMode = "save") {
        const editPopup = document.getElementById('editPopup');
        editPopup.style.width = '500px';
        editPopup.style.height = '400px';


        console.log("openEditPopup called with:", bookmark);//test!!!!!!!!!!!!!!!!!!
        if (!bookmark) {
            console.error('Bookmark data is missing!');
            return;
        }
        mode = popupMode;
        //currentBookmark = {...bookmark};
        currentBookmark = bookmark;
        if(mode === "edit"){
          prevBookmarkName = bookmark.title;
        }
        
        // 기존 데이터 채우기
        bookmarkNameInput.value = currentBookmark.title;
        bookmarkUrlInput.value = currentBookmark.url;
        renderTags(currentBookmark.tags);

        setSaveButtonAction();
    
        // 팝업창 열기
        popupOverlay.classList.remove('hidden');
    };
  
    // 태그 렌더링 함수
    function renderTags(tags) {
      tagList.innerHTML = ''; // 기존 태그 초기화
      tags.forEach((tag, index) => {
        const tagElement = document.createElement('div');
        tagElement.className = 'tag';
        tagElement.innerHTML = `${tag} <span class="remove" data-index="${index}">x</span>`;
        tagList.appendChild(tagElement);
      });
    }
  
    // 태그 제거 이벤트
    tagList.addEventListener('click', function (event) {
      if (event.target.classList.contains('remove')) {
        const index = event.target.dataset.index;
        currentBookmark.tags.splice(index, 1); // 해당 태그 제거
        renderTags(currentBookmark.tags); // 업데이트된 태그 렌더링
      }
    });
  
    // 태그 추가 버튼 클릭 이벤트
    addTagBtn.addEventListener('click', function () {
      const newTag = addTagInput.value.trim();
      if (newTag && !currentBookmark.tags.includes(newTag)) {
        currentBookmark.tags.push(newTag);
        renderTags(currentBookmark.tags);
        addTagInput.value = ''; // 입력창 초기화
      }
    });

    // 저장 버튼 동적 이벤트 설정
    function setSaveButtonAction() {
      // 기존 이벤트 제거
      saveBtn.replaceWith(saveBtn.cloneNode(true));
      const newSaveBtn = document.getElementById('saveBtn');

      // 저장 버튼 클릭 이벤트 설정
      newSaveBtn.addEventListener('click', function () {
          currentBookmark.title = bookmarkNameInput.value.trim();
          currentBookmark.url = bookmarkUrlInput.value.trim();

          if (mode === "save") {
              // 새로운 북마크 저장
              sendBookmarkToBackend(currentBookmark, userEmail)
                  .then(() => {
                      // chrome.storage.sync에 데이터 저장
                      chrome.storage.sync.get({ bookmarks: [] }, function (data) {
                        // 중복 URL 확인
                        const updatedBookmarks = data.bookmarks.filter(
                            (bookmark) => bookmark.url !== currentBookmark.url
                        );
                        updatedBookmarks.push(currentBookmark);

                        chrome.storage.sync.set({ bookmarks: updatedBookmarks }, function () {
                            alert("북마크가 성공적으로 저장되었습니다.");
                            popupOverlay.classList.add('hidden'); // 팝업창 닫기
                            initializeData(); // UI 갱신
                        });
                    });
                  })
                  .catch((error) => {
                      console.error("북마크 저장 중 오류 발생:", error);
                      alert("북마크 저장에 실패했습니다.");
                  });
          } else if (mode === "edit") {
              // 기존 북마크 수정
              sendModifyRequestToBackend(currentBookmark, prevBookmarkName, userEmail)
                  .then(() => {
                      // chrome.storage.sync에 데이터 저장
                      chrome.storage.sync.get({ bookmarks: [] }, function (data) {
                        // 기존 북마크 수정 (URL 기반 비교)
                        const updatedBookmarks = data.bookmarks.map((bookmark) => {
                            if (bookmark.url === prevBookmarkName) {
                                return currentBookmark; // 수정된 북마크로 대체
                            }
                            return bookmark;
                        });

                        chrome.storage.sync.set({ bookmarks: updatedBookmarks }, function () {
                            alert("북마크가 성공적으로 수정되었습니다.");
                            popupOverlay.classList.add('hidden'); // 팝업창 닫기
                            displayAllBookmarks(); // UI 갱신
                        });
                    });
                  })
                  .catch((error) => {
                      console.error("북마크 수정 중 오류 발생:", error);
                      alert("북마크 수정에 실패했습니다.");
                  });
            }
        });
    }

    // 취소 버튼 클릭 이벤트
    cancelBtn.addEventListener('click', function () {
      popupOverlay.classList.add('hidden'); // 팝업창 닫기
    });
  });
  