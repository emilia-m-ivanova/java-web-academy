package commands;

import dao.QuizRepository;
import exception.EntityNotFoundException;
import model.Player;
import model.Quiz;

import java.io.PrintStream;
import java.util.Scanner;

public class DeleteQuizCommand implements Command{
    private QuizRepository quizRepository;
    private Scanner in;
    private Player player;
    private PrintStream out;

    public DeleteQuizCommand(QuizRepository quizRepository, Scanner in, PrintStream out, Player player) {
        this.quizRepository = quizRepository;
        this.in = in;
        this.out = out;
        this.player = player;
    }

    @Override
    public void action() {
        out.printf("Delete Quiz Page%nPlease enter quiz ID: %nPress enter if you want to go back to the main menu.%n");
        String input = in.nextLine().trim();
        if(input.isEmpty()){
            return;
        }
        long id = Long.parseLong(input);
        Quiz chosenQuiz = quizRepository.findByIdAndAuthor(id, player);
        if (chosenQuiz != null) {
            try {
                quizRepository.deleteById(id);
                out.println("Quiz deleted successfully");
            } catch (EntityNotFoundException e) {
                e.getMessage();
            }
        }else{
            out.println("You haven't created a quiz with the given ID.");
            this.action();
        }
    }

    public void updateUser(Player player) {
        this.player = player;
    }

}
