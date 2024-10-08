package kr.brain.our_app.bookmark.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.brain.our_app.bookmark.dto.Bookmark;
import kr.brain.our_app.user.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Controller
public class PageBookmarkController {
    private RestTemplate restTemplate;

    public PageBookmarkController(){
        this.restTemplate = new RestTemplate();
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();             // http 또는 https
        String serverName = request.getServerName();     // 서버 이름 또는 IP
        int serverPort = request.getServerPort();        // 포트 번호
        String contextPath = request.getContextPath();   // 컨텍스트 경로

        // 기본 경로 생성
        return scheme + "://" + serverName + ":" + serverPort + contextPath;
    }

    @GetMapping("/bookmark")
    public String bookmarkPage(Model model, HttpServletRequest request) {
        String baseUrl = getBaseUrl(request);
        String apiUrl = baseUrl + "/api/bookmark";
        Bookmark[] bookmarks = restTemplate.getForObject(apiUrl, Bookmark[].class);
        List<Bookmark> bookmarkList = Arrays.asList(bookmarks);

        model.addAttribute("bookmarkList", bookmarkList);
        model.addAttribute("bookmark", new Bookmark());  // 폼에서 사용할 빈 Bookmark 객체
        return "bookmark";
    }

//    @PostMapping("/bookmark")
//    public String bookmarkRegisterAndList(@ModelAttribute Bookmark bookmark, Model model, HttpServletRequest request) {
//        // 북마크가 제대로 설정되었는지 확인하는 로그 추가
//        System.out.println("Bookmark Name: " + bookmark.getBookmarkName());
//        System.out.println("URL: " + bookmark.getUrl());
//        //System.out.println("User ID: " + bookmark.getUser().getId());
//
//        String baseUrl = getBaseUrl(request);
//        String apiUrl = baseUrl + "/api/bookmark";
//
//        // 랜덤으로 User ID 생성 (1부터 10000 사이)
//        Random random = new Random();
//        Long randomUserId = random.nextLong(10000);
//        bookmark.setUser(new User(randomUserId, "test_user", "test@gmail.com"));
//        bookmark.setId(random.nextLong(10000));//bookmark id 재설정
//
//        // 북마크 등록 POST 요청 보내기
//        restTemplate.postForObject(apiUrl, bookmark, Bookmark.class);
//
//        // 등록 후 북마크 리스트 다시 불러오기
//        Bookmark[] bookmarks = restTemplate.getForObject(apiUrl, Bookmark[].class);
//        List<Bookmark> bookmarkList = Arrays.asList(bookmarks);
//
//        model.addAttribute("bookmarkList", bookmarkList);
//        return "bookmark";
//    }
    @PostMapping("/bookmark")
    public String bookmarkRegisterAndList(@RequestParam("bookmarkName") String bookmarkName,
                                          @RequestParam("url") String url,
                                          Model model, HttpServletRequest request) {
        // 북마크가 제대로 설정되었는지 확인하는 로그 추가
        System.out.println("Bookmark Name: " + bookmarkName);
        System.out.println("URL: " + url);

        String baseUrl = getBaseUrl(request);
        String apiUrl = baseUrl + "/api/bookmark";

        // 새로운 Bookmark 객체 생성
        Bookmark bookmark = new Bookmark();
        bookmark.setBookmarkName(bookmarkName);
        bookmark.setUrl(url);

        // 임의의 User 생성
        Random random = new Random();
        bookmark.setUser(new User(1L, "test_user", "test@gmail.com"));


        // 북마크 등록 POST 요청 보내기
        restTemplate.postForObject(apiUrl, bookmark, Bookmark.class);

        // 등록 후 북마크 리스트 다시 불러오기
        Bookmark[] bookmarks = restTemplate.getForObject(apiUrl, Bookmark[].class);
        List<Bookmark> bookmarkList = Arrays.asList(bookmarks);

        model.addAttribute("bookmarkList", bookmarkList);
        return "bookmark";
    }

}
