package app.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Score")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer scoreId;
    private Integer score;
    @OneToOne
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Score score1 = (Score) o;
        return Objects.equals(scoreId, score1.scoreId) && Objects.equals(score, score1.score) && Objects.equals(user, score1.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scoreId, score, user);
    }

    @Override
    public String toString() {
        return "Score{" +
                "scoreId=" + scoreId +
                ", score=" + score +
                ", user=" + user +
                '}';
    }

    public Integer getScoreId() {
        return scoreId;
    }

    public void setScoreId(Integer scoreId) {
        this.scoreId = scoreId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
