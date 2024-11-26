const userEmail = "test1@gmail.com";
// 팝업 오버레이 관리 로직
document.addEventListener('DOMContentLoaded', function () {
    const bookmarkNameInput = document.querySelector('.div9'); // 이름 설정 input
    const tagList = document.querySelector('.tag'); // 태그 리스트 컨테이너
    const addTagInput = document.querySelector('.div7'); // 태그 추가 input
    const addTagBtn = document.querySelector('.addtag'); // 태그 추가 버튼
    const saveBtn = document.querySelector('.div'); // 완료 버튼
    const cancelBtn = document.querySelector('.div3'); // 삭제 버튼
    
    let currentTags = [];

    const params = new URLSearchParams(window.location.search);
    const title = params.get('title') || '';
    const url = params.get('url') || '';
    const tags = JSON.parse(params.get('tags') || '[]');
    //const tags = params.get('tags');
    
    const mode = params.get('mode') || 'save'; // mode 파라미터 확인

    console.log("dom road 후 parameter 체크",params, title, url, tags, mode);//test

    // 요소 초기화
    bookmarkNameInput.value = title;
    currentTags = [...tags]; // 태그 배열 초기화
    renderTags(tags);
    //renderTags(currentTags);

    if (mode === 'edit') {
        // 수정 모드일 경우
        saveBtn.textContent = '완료'; // 버튼 텍스트 변경
        saveBtn.style.backgroundColor = '#4CAF50'; // 수정 완료 스타일
        cancelBtn.textContent = '취소'; // 버튼 텍스트 변경
    } else {
        // 저장 모드일 경우 (기본값)
        saveBtn.textContent = '저장'; // 버튼 텍스트 변경
        saveBtn.style.backgroundColor = '#007BFF'; // 저장 스타일
        cancelBtn.textContent = '취소'; // 버튼 텍스트 변경
    }

        // 완료 버튼 이벤트 설정
        saveBtn.addEventListener('click', function () {
            const updatedBookmark = {
                title: bookmarkNameInput.value.trim(),
                url: url, // URL은 수정하지 않도록 설정
                tags: currentTags, // 현재 태그 배열
            };
    
            if (mode === 'save') {
                // 저장 모드: 새로운 북마크 저장
                sendBookmarkToBackend(updatedBookmark, userEmail)
                    .then(() => {
                        alert('북마크가 성공적으로 저장되었습니다.');
                        window.location.href = 'search_page.html';
                    })
                    .catch((error) => {
                        console.error('북마크 저장 중 오류 발생:', error);
                        alert('북마크 저장에 실패했습니다.');
                    });
            } else if (mode === 'edit') {
                // 수정 모드: 기존 북마크 수정
                sendModifyRequestToBackend(updatedBookmark, title, userEmail)
                    .then(() => {
                        alert('북마크가 성공적으로 수정되었습니다.');
                        window.location.href = 'search_page.html';
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
                    window.location.href = 'search_page.html'; // 수정 취소 시 이전 페이지로 이동
                }
            } else {
                window.location.href = 'search_page.html'; // 저장 취소 시 이전 페이지로 이동
            }
        });


        // 태그 추가 이벤트
        addTagBtn.addEventListener('click', function () {
            const newTag = addTagInput.value.trim();
            if (newTag && !currentTags.includes(newTag)) {
                currentTags.push(newTag);
                renderTags(currentTags);
                addTagInput.value = ''; // 입력창 초기화
            }
        });

    });        
    // 태그 렌더링 함수
    function renderTags(tags) {
        tagList.innerHTML = ''; // 기존 태그 초기화
        tags.forEach((tag) => {
            const tagContainer = document.createElement('div');
            tagContainer.className = '_1'; // 태그 컨테이너 클래스
            tagContainer.innerHTML = `
                <div class="div5">${tag}</div>
                <img class="x" src="x0.svg" alt="삭제" />
            `;
    
            // 태그 삭제 이벤트
            tagContainer.querySelector('.x').addEventListener('click', function () {
                currentTags = currentTags.filter((t) => t !== tag);
                renderTags(currentTags);
            });
    
            tagList.appendChild(tagContainer);
        });
        console.log("태그 렌더링",tagList);//test
    }        




  