package model;

public class QuizResult extends AbstractEntity<Long,QuizResult>{
    private User player; //- the reference to the User (Player) taking the 'Quiz;
    private Quiz quiz; //- reference to the Quiz taken;
    private int score; //- integer number (sum of Answer scores for all answered questions);

    public QuizResult() {
    }

    public QuizResult(User player, Quiz quiz, int score) {
        this.player = player;
        this.quiz = quiz;
        this.score = score;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QuizResult{");
        sb.append("id=").append(getId());
        sb.append(", created=").append(getCreated());
        sb.append(", modified=").append(getModified());
        sb.append(", player=").append(player.getUsername());
        sb.append(", quiz=").append(quiz.getDescription());
        sb.append(", score=").append(score);
        sb.append('}');
        return sb.toString();
    }
}
