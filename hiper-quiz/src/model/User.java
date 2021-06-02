package model;

import java.util.List;

public class User extends AbstractEntity<Long,User>{
    private String username;// - string 2 to 15 characters long - word characters only, unique within the system, cannot be changed;
    private String email;// - should be valid email address, unique within the system, cannot be changed;
    private String password;// - string 8 to 15 characters long, at least one digit, one capital letter, and one sign different than letter or digit, NOT sent back to the User clients for security reasons;
    private Gender gender;// - MALE / FEMALE enumeration;
    private Role role = Role.PLAYER;// - PLAYER or ADMIN enumeration, PLAYER by default, editable only by Administrators;
    private String picture;// of the user (optional) - valid URL, if missing should ne substituted with an avatar according to the gender;
    private String description;// (optional) - string 20 - 250 characters long;
    private String metadata;// (optional) - string up to 512 characters long, visible and editable only by Administrators;
    private boolean status = true;// - boolean - validity status of the user account;
    private List<Quiz> quizzes;// - list of all Quizzes created by the current User;

    public User() {
    }

    public User(String username, String email, String password, Gender gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.picture = gender.equals(Gender.FEMALE)? "female avatar" : "male avatar";
    }

    public User(String username, String email, String password, Gender gender, String picture, String description) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.picture = picture;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Role getRole() {
        return role;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(getId());
        sb.append(", created=").append(getCreated());
        sb.append(", modified=").append(getModified());
        sb.append(", username='").append(username).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", gender=").append(gender);
        sb.append(", role=").append(role);
        sb.append(", picture='").append(picture).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", metadata='").append(metadata).append('\'');
        sb.append(", status=").append(status);
        sb.append(", quizzes=").append(quizzes);
        sb.append('}');
        return sb.toString();
    }
}
