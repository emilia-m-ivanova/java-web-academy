package model;

import java.util.ArrayList;
import java.util.List;

public class Quiz extends AbstractEntity<Long,Quiz>{

    private String title; // - string 2 to 80 characters long;
    private User author; // - the User that created the Quiz;
    private String description; // - string 20 - 250 characters long, supporting Markdown syntax;
    private List<Question> questions = new ArrayList<>(); // - list of Question entities (containing the answers with their scores too);
    private int expectedDuration; // - integer number in minutes;
    private String picture = "default picture"; // (optional) - best representing the Quiz, valid URL to a picture, if missing a placeholder picture should be used;
    private String tags; // - string including comma separated tags, allowing to find the Quizes by quick search;

    public Quiz() {
    }

    public Quiz(String title, User author, String description, List<Question> questions, int expectedDuration) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.questions = questions;
        this.expectedDuration = expectedDuration;
    }

    public Quiz(String title, User author, String description, int expectedDuration) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.expectedDuration = expectedDuration;
    }

    public Quiz(String title, User author, String description, List<Question> questions, int expectedDuration, String picture, String tags) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.questions = questions;
        this.expectedDuration = expectedDuration;
        this.picture = picture;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getExpectedDuration() {
        return expectedDuration;
    }

    public void setExpectedDuration(int expectedDuration) {
        this.expectedDuration = expectedDuration;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void addQuestion(Question question){
        this.questions.add(question);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Quiz{");
        sb.append("id=").append(getId());
        sb.append(", created=").append(getCreated());
        sb.append(", modified=").append(getModified());
        sb.append(", title='").append(title).append('\'');
        sb.append(", author=").append(author.getUsername());
        sb.append(", description='").append(description).append('\'');
        sb.append(", expectedDuration=").append(expectedDuration);
        sb.append(", picture='").append(picture).append('\'');
        sb.append(", tags='").append(tags).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
