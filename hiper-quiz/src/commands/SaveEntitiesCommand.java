package commands;

import dao.PlayerRepository;
import dao.QuizRepository;
import dao.QuizResultRepository;
import model.AllCollections;
import model.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SaveEntitiesCommand implements Command {
    private PlayerRepository playerRepository;
    private QuizRepository quizRepository;
    private QuizResultRepository quizResultRepository;
    private OutputStream out;

    public SaveEntitiesCommand(OutputStream out,
                               PlayerRepository playerRepository,
                               QuizRepository quizRepository,
                               QuizResultRepository quizResultRepository
    ) {
        this.out = out;
        this.playerRepository = playerRepository;
        this.quizRepository = quizRepository;
        this.quizResultRepository = quizResultRepository;
    }
    @Override
    public void action() {
        AllCollections allCollections = new AllCollections(
                playerRepository.findAll(),
                quizRepository.findAll(),
                quizResultRepository.findAll());
        try(ObjectOutputStream oos = new ObjectOutputStream(out)){
            oos.writeObject(allCollections);
           // return "All collections saved successfully";
        } catch (IOException e) {
            System.out.println(e.getMessage());
          //  return "Error writing collections to file";
        }
    }

}
