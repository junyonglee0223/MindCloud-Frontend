document.getElementById("bookmark-form").addEventListener("submit", (e) => {
  e.preventDefault();
  const title = document.getElementById("title").value;
  const url = document.getElementById("url").value;

  // 북마크 저장 로직
  chrome.storage.sync.get("bookmarks", ({ bookmarks }) => {
      const newBookmarks = [...(bookmarks || []), { title, url }];
      chrome.storage.sync.set({ bookmarks: newBookmarks }, () => {
          console.log("북마크가 저장되었습니다.");
          // 입력 필드 초기화
          document.getElementById("title").value = "";
          document.getElementById("url").value = "";
      });
  });
});
