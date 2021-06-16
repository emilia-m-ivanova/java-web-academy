package model;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;

@Entity
public class Administrator extends User{
    @Transient
    List<Quiz>quizzesBlocked; // - list of Quizzes the Administrator has temporarily blocked for non-compliance with the site policies;
}
