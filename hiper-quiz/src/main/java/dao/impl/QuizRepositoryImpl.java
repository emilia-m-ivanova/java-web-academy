package dao.impl;

import dao.KeyGenerator;
import dao.QuizRepository;
import model.Quiz;
import model.User;

public class QuizRepositoryImpl extends RepositoryMemoryImpl<Long, Quiz> implements QuizRepository {
    public QuizRepositoryImpl(KeyGenerator<Long> keyGenerator) {
        super(keyGenerator);
    }

    @Override
    public Quiz findByIdAndAuthor(long id, User author) {
        return this.findAll().stream()
                .filter(e->e.getAuthor().equals(author) && e.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
