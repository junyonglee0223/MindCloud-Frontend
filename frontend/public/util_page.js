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
    window.location.href = `check_page.html?${params.toString()}`;
}