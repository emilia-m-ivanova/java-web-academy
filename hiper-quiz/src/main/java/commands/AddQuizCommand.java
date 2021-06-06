package commands;

import dao.QuizRepository;
import exception.EntityAlreadyExistsException;
import model.Quiz;
import util.LoggedUser;

import java.io.PrintStream;
import java.util.Scanner;

public class AddQuizCommand implements Command{
    private QuizRepository quizRepository;
    private Scanner in;
    private PrintStream out;

    public AddQuizCommand(QuizRepository quizRepository, Scanner in, PrintStream out) {
        this.quizRepository = quizRepository;
        this.in = in;
        this.out = out;
    }

    @Override
    public void action() {
        out.printf("Add Quiz Page%nPress enter if you want to go back to the main menu.%nTitle: %n");
        String title = in.nextLine().trim();
        if(title.isEmpty()){
            return;
        }
        //TODO: add validation for title and description
        out.println("Description: ");
        String description = in.nextLine().trim();
        out.println("Expected Duration: ");
        int expectedDuration = -1;
        while (expectedDuration < 0) {
            try {
                expectedDuration = Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                out.println("Please enter a positive number");
            }
        }
        try {
            Quiz quiz = quizRepository.create(new Quiz(title, LoggedUser.getLoggedUser(), description, expectedDuration));
            new AddQuestionsCommand(quizRepository,in,out,quiz.getId()).action();
        } catch (EntityAlreadyExistsException e) {
            e.getMessage();
        }
    }
}
