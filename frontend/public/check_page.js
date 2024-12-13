//const userEmail = "test1@gmail.com";
// 팝업 오버레이 관리 로직
document.addEventListener('DOMContentLoaded', function () {
    let userEmail;
    chrome.storage.sync.get("userData", ({ userData }) => {
        if (userData && userData.email) {
          userEmail = userData.email; // Retrieve the email dynamically
          console.log("User email retrieved:", userEmail);
    
          // Example usage
          loadBookmarks(userEmail);
        } else {
          console.error("User email not found in chrome.storage.sync");
        }
      });
    const bookmarkNameInput = document.querySelector('.div9'); // 이름 설정 input
    const tagList = document.querySelector('.tag-list'); // 태그 리스트 컨테이너
    const addTagInput = document.querySelector('.div7'); // 태그 추가 input
    const addTagBtn = document.querySelector('.addtag'); // 태그 추가 버튼
    const saveBtn = document.querySelector('.div'); // 완료 버튼
    const cancelBtn = document.querySelector('.div3'); // 삭제 버튼

    // 전역 currentTags
    let currentTags = [];

    const params = new URLSearchParams(window.location.search);
    const title = params.get('title') || '';
    const url = params.get('url') || '';
    const tags = JSON.parse(params.get('tags') || '[]');
    const mode = params.get('mode') || 'save'; // mode 파라미터 확인
    // //썸네일 생성
    // const imageUrl = fetchThumbnailUrl(url);
    // console.log("썸네일 edit 창 생성시 탐색",imageUrl);//test
    

    console.log("DOM 로드 후 파라미터 체크:", params, title, url, tags, mode); // 테스트

    // 요소 초기화
    bookmarkNameInput.value = title;
    currentTags = [...tags]; // 태그 배열 초기화
    renderTags(); // 초기 태그 렌더링

    if (mode === 'edit') {
        // 수정 모드일 경우
        saveBtn.textContent = '완료'; // 버튼 텍스트 변경
        saveBtn.style.backgroundColor = '#3e92f8'; // 수정 완료 스타일
        saveBtn.style.color = '#ffffff'; // 수정 완료 스타일

        cancelBtn.textContent = '취소'; // 버튼 텍스트 변경
        cancelBtn.style.backgroundColor = '#E8EDF2'; // 버튼 텍스트 변경
    } else {
        // 저장 모드일 경우 (기본값)
        saveBtn.textContent = '저장'; // 버튼 텍스트 변경
        saveBtn.style.backgroundColor = '#3e92f8'; // 저장 스타일
        saveBtn.style.color = '#ffffff'; // 수정 완료 스타일

        cancelBtn.textContent = '취소'; // 버튼 텍스트 변경
        cancelBtn.style.backgroundColor = '#E8EDF2'; // 버튼 텍스트 변경
    }

    // 완료 버튼 이벤트 설정
    saveBtn.addEventListener('click', async function () {
        const updatedBookmark = {
            title: bookmarkNameInput.value.trim(),
            url: url, // URL은 수정하지 않도록 설정
            tags: currentTags, // 현재 태그 배열
        };

        if (mode === 'save') {
            // 저장 모드: 새로운 북마크 저장

            //썸네일 생성
            const imageUrl = await fetchThumbnailUrl(url);
            updatedBookmark.imageUrl = imageUrl;

            sendBookmarkToBackend(updatedBookmark, userEmail)
                .then(() => {
                    alert('북마크가 성공적으로 저장되었습니다.');
                    window.close();
                    //window.location.href = 'search_page.html';
                })
                .catch((error) => {
                    console.error('북마크 저장 중 오류 발생:', error);
                    alert('북마크 저장에 실패했습니다.');
                });
        } else if (mode === 'edit') {
            //썸네일 생성
            const imageUrl = await fetchThumbnailUrl(url);
            updatedBookmark.imageUrl = imageUrl;

            console.log(updatedBookmark);//test
            
            // 수정 모드: 기존 북마크 수정
            sendModifyRequestToBackend(updatedBookmark, title, userEmail)
                .then(() => {
                    alert('북마크가 성공적으로 수정되었습니다.');
                    window.close();
                    //window.location.href = 'search_page.html';
                })
                .catch((error) => {
                    console.error('북마크 수정 중 오류 발생:', error);
                    alert('북마크 수정에 실패했습니다.');
                });
        }
    });

    // 삭제 버튼 이벤트 설정
    cancelBtn.addEventListener('click', function () {
        if (mode === 'edit') {
            if (confirm('수정을 취소하시겠습니까?')) {
                window.close();
//                window.location.href = 'search_page.html'; // 수정 취소 시 이전 페이지로 이동
            }
        } else {
            window.close();
            //window.location.href = 'search_page.html'; // 저장 취소 시 이전 페이지로 이동
        }
    });

    // 태그 추가 이벤트
    addTagBtn.addEventListener('click', function () {
        const newTag = addTagInput.value.trim();
        if (newTag && !currentTags.includes(newTag)) {
            currentTags.push(newTag); // 새로운 태그 추가
            renderTags(); // 태그 재렌더링
            addTagInput.value = ''; // 입력창 초기화
        }
    });

    // 태그 렌더링 함수 (수정됨)
    function renderTags() {
        tagList.innerHTML = ''; // 기존 태그 초기화

        //console.log("현재 태그 상태 체크:", currentTags); // 테스트
        currentTags.forEach((tag) => {
            const tagContainer = document.createElement('div');
            tagContainer.className = '_1'; // 태그 컨테이너 클래스
            tagContainer.innerHTML = `
                <div class="div5">${tag}</div>
                <img class="x" src="x0.svg" alt="삭제" />
            `;

            // 태그 삭제 이벤트
            tagContainer.querySelector('.x').addEventListener('click', function () {
                // currentTags 배열에서 해당 태그 삭제
                currentTags = currentTags.filter((t) => t !== tag);

                //console.log("태그 삭제 결과 테스트:", currentTags); // 삭제 후 상태 확인

                // 삭제된 태그로 렌더링 갱신
                renderTags();
            });

            tagList.appendChild(tagContainer);

        });

        console.log("태그 렌더링 완료:", tagList); // 렌더링 결과 확인 (테스트)
    }
});


async function fetchThumbnailUrl(url) {
    try {
        console.log(`Fetching thumbnail for URL: ${url}`); // 디버깅 로그 추가
        const response = await fetch(url);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
  
        const html = await response.text();
        const doc = new DOMParser().parseFromString(html, 'text/html');
        const metaTags = doc.getElementsByTagName('meta');
        
        for (let meta of metaTags) {
            if (meta.getAttribute('property') === 'og:image' || meta.getAttribute('name') === 'twitter:image') {
                const thumbnailUrl = meta.getAttribute('content');
                console.log(`Found thumbnail URL: ${thumbnailUrl}`); // 디버깅 로그 추가
                return thumbnailUrl;
            }
        }
        console.warn('No thumbnail URL found in meta tags');
        return "thumbnail-div-box-1-img0.png"; // 기본 이미지 경로
    } catch (error) {
        console.error('썸네일 URL 추출 중 오류 발생:', error);
        return "thumbnail-div-box-1-img0.png"; // 기본 이미지 경로
    }
  }