package kr.brain.our_app.tag.dto;

public class Tag {
    private Long id;
    private String tagname;

    public Tag() {
        this.tagname = "기타태그"; // 기본값
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }
}

