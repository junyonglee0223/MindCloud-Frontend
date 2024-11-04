//package kr.brain.our_app.bookmark.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import kr.brain.our_app.bookmark.domain.TagBookmark;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Controller
//public class PageTagBookmarkController {
//    private final RestTemplate restTemplate;
////    private final BookmarkService bookmarkService;
////    private final TagService tagService;
//
////    @Autowired
////    public PageTagBookmarkController(BookmarkService bookmarkService, TagService tagService) {
////        this.restTemplate = new RestTemplate();
////        this.bookmarkService = bookmarkService;
////        this.tagService = tagService;
////    }
//    //@Autowired
//    public PageTagBookmarkController() {
//        this.restTemplate = new RestTemplate();
//    }
//
//    private String getBaseUrl(HttpServletRequest request) {
//        String scheme = request.getScheme();
//        String serverName = request.getServerName();
//        int serverPort = request.getServerPort();
//        String contextPath = request.getContextPath();
//        return scheme + "://" + serverName + ":" + serverPort + contextPath;
//    }
//
//    @GetMapping("/tagbookmark")
//    public String bookmarkPage(Model model, HttpServletRequest request) {
//        String baseUrl = getBaseUrl(request);
//        String apiUrl = baseUrl + "/api/tagbookmark/all";
//        TagBookmark[] tagBookmarks = restTemplate.getForObject(apiUrl, TagBookmark[].class);
//        System.out.println("TagBookmarks: " + Arrays.toString(tagBookmarks));
//
//
//        //List<TagBookmark> tagBookmarkList = (tagBookmarks != null) ? Arrays.asList(tagBookmarks) : new ArrayList<>();
//        List<TagBookmark> tagBookmarkList = Arrays.asList(tagBookmarks);
//
//        model.addAttribute("tagBookmarks", tagBookmarkList);
//        model.addAttribute("tagBookmark", new TagBookmark());
//
//        return "tagbookmark";
//    }
//
//    @PostMapping("/tagbookmark")
//    public String handleTagBookmarkCreation(
//            @RequestParam Long tagId,
//            @RequestParam Long bookmarkId,
//            HttpServletRequest request) {
//
//        // API 요청을 통한 태그와 북마크 데이터 가져오기
//        String baseUrl = getBaseUrl(request);
//
//        // 태그를 API로부터 가져오기
//        String apiUrl = baseUrl + "/api/tagbookmark";
//        restTemplate.postForObject(apiUrl, null, String.class, tagId, bookmarkId);
//
//        // 태그와 북마크 페이지로 리디렉션
//        return "redirect:/tagbookmark";
//    }
//
////
////    @PostMapping("/tagbookmark")
////    public String handleTagBookmarkCreation(
////            @RequestParam Long tagId,
////            @RequestParam Long bookmarkId,
////            Model model,
////            HttpServletRequest request) {
////
////        // 입력받은 tagId와 bookmarkId로 Tag와 Bookmark 찾기
////        Tag tag = tagService.findTagById(tagId)
////                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 태그를 찾을 수 없습니다."));
////        Bookmark bookmark = bookmarkService.findBookmarkById(bookmarkId)
////                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 북마크를 찾을 수 없습니다."));
////
////        // API 요청을 위한 URL 생성 (기존 코드와 동일)
////        String baseUrl = getBaseUrl(request);
////        String apiUrl = baseUrl + "/api/tagbookmark";
////
////        // RestTemplate을 사용해 Tag와 Bookmark를 결합하는 요청 전송
////        restTemplate.postForObject(apiUrl + "?tagId=" + tagId, bookmark, TagBookmark.class);
////
////        // 태그와 북마크 페이지로 리디렉션 (기존 코드와 동일)
////        return "tagbookmark";
////    }
//
//
////    @PostMapping("/tagbookmark")
////    public String handleTagBookmarkCreation(
////            @RequestParam Long tagId,
////            @RequestParam Long bookmarkId,
////            HttpServletRequest request) {
////
////        // API 요청을 통한 태그와 북마크 데이터 가져오기
////        String baseUrl = getBaseUrl(request);
////
////        // 태그를 API로부터 가져오기
////        String tagApiUrl = baseUrl + "/api/tags/" + tagId;
////        Tag tag = restTemplate.getForObject(tagApiUrl, Tag.class);
////        if (tag == null) {
////            throw new IllegalArgumentException("해당 ID의 태그를 찾을 수 없습니다.");
////        }
////
////        // 북마크를 API로부터 가져오기
////        String bookmarkApiUrl = baseUrl + "/api/bookmarks/" + bookmarkId;
////        Bookmark bookmark = restTemplate.getForObject(bookmarkApiUrl, Bookmark.class);
////        if (bookmark == null) {
////            throw new IllegalArgumentException("해당 ID의 북마크를 찾을 수 없습니다.");
////        }
////
////        // API 요청을 위한 URL 생성
////        String apiUrl = baseUrl + "/api/tagbookmark";
////
////        // RestTemplate을 사용해 Tag와 Bookmark를 결합하는 요청 전송
////        restTemplate.postForObject(apiUrl + "?tagId=" + tagId, bookmark, TagBookmark.class);
////
////        // 태그와 북마크 페이지로 리디렉션
////        return "redirect:/tagbookmark";
////    }
//}