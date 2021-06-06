package commands;

import dao.QuizRepository;
import exception.EntityNotFoundException;
import model.Player;
import model.Quiz;

import java.util.Scanner;

public class DeleteQuizCommand implements Command{
    private QuizRepository quizRepository;
    private Scanner in;
    private Player player;

    public DeleteQuizCommand(QuizRepository quizRepository, Scanner in, Player player) {
        this.quizRepository = quizRepository;
        this.in = in;
        this.player = player;
    }

    @Override
    public void action() {
        System.out.printf("Delete Quiz Page%nPlease enter quiz ID: %n");
        long id = Long.parseLong(in.nextLine().trim());
        Quiz chosenQuiz = quizRepository.findByIdAndAuthor(id, player);
        if (chosenQuiz != null) {
            try {
                quizRepository.deleteById(id);
                System.out.println("Quiz deleted successfully");
            } catch (EntityNotFoundException e) {
                e.getMessage();
            }
        }else{
            System.out.println("You haven't created a quiz with the given ID.");
            this.action();
        }
    }
}
