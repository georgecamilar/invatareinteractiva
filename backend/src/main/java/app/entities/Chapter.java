package app.entities;

import javax.persistence.*;

@Entity
@Table
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer chapterId;
    private String title;

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
