package model;

import java.util.ArrayList;
import java.util.List;

public class Player extends User{
    private List<QuizResult>results = new ArrayList<>(); // - list of all QuizResults from Quizzes the Player has taken;
    private int overallScore;// - integer number representing Player's experience (sum of all scores form Quizzes taken), to be transformed to Rank enumeration, using formula chosen by you.

    public Player(String username, String email, String password, Gender gender) {
        super(username, email, password, gender);
    }

    public Player(String username, String email, String password, Gender gender, String picture, String description) {
        super(username, email, password, gender, picture, description);
    }

    public int getOverallScore(){
        return this.results.stream()
                .mapToInt(e->e.getScore())
                .sum();
    }

    public void takeQuiz(QuizResult quizResult){
        results.add(quizResult);
    }

    @Override
    public String toString() {
        return this.getUsername();
    }
}
