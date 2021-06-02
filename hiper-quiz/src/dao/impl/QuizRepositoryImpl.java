package dao.impl;

import dao.KeyGenerator;
import dao.QuizRepository;
import model.Quiz;

public class QuizRepositoryImpl extends RepositoryMemoryImpl<Long, Quiz> implements QuizRepository {
    public QuizRepositoryImpl(KeyGenerator<Long> keyGenerator) {
        super(keyGenerator);
    }
}
