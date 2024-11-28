export function openEditPage(bookmark, mode = 'save') {
    console.log("인자 전달 테스트 확인0",bookmark, mode);//test
    const params = new URLSearchParams({
        title: bookmark.title || '',
        url: bookmark.url || '',
        tags: JSON.stringify(bookmark.tags || []),
        //tags: bookmark.tags,


        mode: mode, // mode 추가
    });
    console.log("인자 전달 테스트 확인",params);//test
    chrome.tabs.create({
        url: chrome.runtime.getURL(`../check_page.html?${params.toString()}`) // 열고자 하는 HTML 파일 경로
      });  
    //window.location.href = `../check_page.html?${params.toString()}`;
}