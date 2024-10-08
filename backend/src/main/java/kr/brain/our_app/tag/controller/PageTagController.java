package kr.brain.our_app.tag.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.brain.our_app.bookmark.dto.Bookmark;
import kr.brain.our_app.bookmark.dto.TagBookmark;
import kr.brain.our_app.tag.dto.Tag;
import kr.brain.our_app.user.dto.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Controller
public class PageTagController {
    private final RestTemplate restTemplate;

    public PageTagController(){
        this.restTemplate = new RestTemplate();
    }

    // 서버 기본 URL을 동적으로 가져오는 메서드
    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();             // http 또는 https
        String serverName = request.getServerName();     // 서버 이름 또는 IP
        int serverPort = request.getServerPort();        // 포트 번호
        String contextPath = request.getContextPath();   // 컨텍스트 경로

        // 기본 경로 생성
        return scheme + "://" + serverName + ":" + serverPort + contextPath;
    }

    // 사용자 목록 페이지 렌더링
    @GetMapping("/tag")
    public String tagPage(Model model, HttpServletRequest request) {

        // /api/users/all 경로로 GET 요청하여 사용자 데이터 가져옴
        String baseUrl = getBaseUrl(request);
        String apiUrl = baseUrl + "/api/tag"; // UserController의 엔드포인트
        Tag[] tags = restTemplate.getForObject(apiUrl,Tag[].class);
        List<Tag> tagList = Arrays.asList(tags);


        model.addAttribute("tagList", tagList);
        model.addAttribute("tag", new Tag());  // 폼에서 사용할 빈 Bookmark 객체
        return "tag_list";  // src/main/resources/templates/user_list.html 파일을 렌더링
    }

    //tag 생성하는 로직
    @PostMapping("/tag")
    public String bookmarkRegisterAndList(@RequestParam("tagName") String TagName,
                                          Model model, HttpServletRequest request) {
        // 북마크가 제대로 설정되었는지 확인하는 로그 추가
        System.out.println("Tag Name: " + TagName);

        String baseUrl = getBaseUrl(request);
        String apiUrl = baseUrl + "/api/tag";

        // 새로운 Bookmark 객체 생성
        Tag tag = new Tag();
        tag.setTagname(TagName);

        // 임의의 User 생성
        Random random = new Random();
        tag.setUser(new User(1L, "test_user", "test@gmail.com"));


        // 태그 등록 POST 요청 보내기
        restTemplate.postForObject(apiUrl, tag, Tag.class);

        // 등록 후 태그 리스트 다시 불러오기
        Tag[] tags = restTemplate.getForObject(apiUrl, Tag[].class);
        List<Tag> tagList = Arrays.asList(tags);

        model.addAttribute("tagList", tagList);
        return "tag_list";
    }
}

