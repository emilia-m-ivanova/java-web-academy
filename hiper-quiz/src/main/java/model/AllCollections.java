package model;

import java.io.Serializable;
import java.util.List;

public class AllCollections implements Serializable {
    private List<Player> players;
    private List<Quiz> quizzes;
    private List<QuizResult> quizResults;

    public AllCollections(List<Player> players, List<Quiz> quizzes, List<QuizResult> quizResults) {
        this.players = players;
        this.quizzes = quizzes;
        this.quizResults = quizResults;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public List<QuizResult> getQuizResults() {
        return quizResults;
    }

    public void setQuizResults(List<QuizResult> quizResults) {
        this.quizResults = quizResults;
    }
}
