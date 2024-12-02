export function openEditPage(bookmark, mode = 'save') {
    console.log("인자 전달 테스트 확인0", bookmark, mode); // test

    const params = new URLSearchParams({
        title: bookmark.title || '',
        url: bookmark.url || '',
        tags: JSON.stringify(bookmark.tags || []),
        mode: mode, // mode 추가
    });

    console.log("인자 전달 테스트 확인", params); // test

    // 화면 크기 계산
    chrome.windows.getCurrent((currentWindow) => {
        const screenWidth = currentWindow.width || window.screen.availWidth; // 현재 창의 너비
        const screenHeight = currentWindow.height || window.screen.availHeight; // 현재 창의 높이

        chrome.windows.create({
            url: chrome.runtime.getURL(`../check_page.html?${params.toString()}`), // 열고자 하는 HTML 파일 경로
            type: 'popup', // 팝업 형태로 열기 (제목 표시줄과 주소 표시줄 없음)
            width: 518, // 창 너비
            height: 271, // 창 높이
            top: 120, // 화면 상단
            left: screenWidth - 518, // 화면 오른쪽 끝에 정렬
        });
    });
}
