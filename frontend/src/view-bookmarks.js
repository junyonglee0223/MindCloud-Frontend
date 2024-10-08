document.addEventListener("DOMContentLoaded", () => {
  chrome.storage.sync.get("bookmarks", ({ bookmarks }) => {
      const bookmarkList = document.getElementById("bookmark-list");
      if (bookmarks) {
          bookmarks.forEach(({ title, url }) => {
              const listItem = document.createElement("li");
              listItem.innerHTML = `<a href="${url}" target="_blank">${title}</a>`;
              bookmarkList.appendChild(listItem);
          });
      }
  });
});
