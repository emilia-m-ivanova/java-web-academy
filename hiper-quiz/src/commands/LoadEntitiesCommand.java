package commands;

import dao.PlayerRepository;
import dao.QuizRepository;
import dao.QuizResultRepository;
import exception.EntityAlreadyExistsException;
import model.AllCollections;

import java.io.*;

public class LoadEntitiesCommand implements Command{
    private PlayerRepository playerRepository;
    private QuizRepository quizRepository;
    private QuizResultRepository quizResultRepository;
    private InputStream in;

    public LoadEntitiesCommand(InputStream in,
                               PlayerRepository playerRepository,
                               QuizRepository quizRepository,
                               QuizResultRepository quizResultRepository
    ) {
        this.in = in;
        this.playerRepository = playerRepository;
        this.quizRepository = quizRepository;
        this.quizResultRepository = quizResultRepository;
    }
    @Override
    public void action() {
        try(ObjectInputStream ois = new ObjectInputStream(in)){
            AllCollections allCollections = (AllCollections) ois.readObject();
            long playersCount = playerRepository.createFromMemory(allCollections.getPlayers());
            playerRepository.updateKeyGenerator(playersCount);
            long quizzesCount = quizRepository.createFromMemory(allCollections.getQuizzes());
            quizRepository.updateKeyGenerator(quizzesCount);
            long resultsCount = quizResultRepository.createFromMemory(allCollections.getQuizResults());
            quizResultRepository.updateKeyGenerator(resultsCount);
            // return "All collections loaded successfully";
        } catch (IOException | ClassNotFoundException | EntityAlreadyExistsException e) {
            e.printStackTrace();
           // return "Error reading collections from file";
        } // return "Error adding entities to repository";

    }
}
