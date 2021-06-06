package commands;

import dao.QuizRepository;
import exception.EntityAlreadyExistsException;
import model.Player;
import model.Quiz;

import java.util.Scanner;

public class AddQuizCommand implements Command{
    private QuizRepository quizRepository;
    private Scanner in;
    private Player player;

    public AddQuizCommand(QuizRepository quizRepository, Scanner in, Player player) {
        this.quizRepository = quizRepository;
        this.in = in;
        this.player = player;
    }

    @Override
    public void action() {
        System.out.printf("Add Quiz Page%nTitle: %n");
        String title = in.nextLine().trim();
        System.out.println("Description: ");
        String description = in.nextLine().trim();
        System.out.println("Expected Duration: ");
        int expectedDuration = -1;
        while (expectedDuration < 0) {
            try {
                expectedDuration = Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                System.err.println("Please enter a positive number");
            }
        }
        try {
            Quiz quiz = quizRepository.create(new Quiz(title, player, description, expectedDuration));
            new AddQuestionsCommand(quizRepository,in,player,quiz.getId()).action();
        } catch (EntityAlreadyExistsException e) {
            e.getMessage();
        }
    }
}
