package model;

import java.util.List;

public class Administrator extends User{
    List<Quiz>quizzesBlocked; // - list of Quizzes the Administrator has temporarily blocked for non-compliance with the site policies;
}
