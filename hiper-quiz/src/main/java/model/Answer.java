package model;

public class Answer extends AbstractEntity<Long,Answer>{
    private Question question; //- reference to the Question to which the Answer belongs;
    private String text; //- string 2 - 150 characters long, supporting Markdown syntax;
    private String picture; //(optional) - if the Answer includes picture, valid URL;
    private int score; //- integer number (could be negative too);

    public Answer() {
    }

    public Answer(Question question, String text, int score) {
        this.question = question;
        this.text = text;
        this.score = score;
    }

    public Answer(Question question, String text, String picture, int score) {
        this.question = question;
        this.text = text;
        this.picture = picture;
        this.score = score;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Answer{");
        sb.append("id=").append(getId());
        sb.append(", created=").append(getCreated());
        sb.append(", modified=").append(getModified());
        sb.append(", question=").append(question.getText());
        sb.append(", text='").append(text).append('\'');
        sb.append(", picture='").append(picture).append('\'');
        sb.append(", score=").append(score);
        sb.append('}');
        return sb.toString();
    }
}
