package dao;

import model.Quiz;
import model.User;

public interface QuizRepository extends Repository<Long, Quiz>{
    Quiz findByIdAndAuthor(long id, User author);
}
