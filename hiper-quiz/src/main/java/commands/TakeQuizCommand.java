package commands;

import dao.QuizRepository;
import dao.QuizResultRepository;
import exception.EntityAlreadyExistsException;
import model.Player;
import model.Question;
import model.Quiz;
import model.QuizResult;
import util.LoggedUser;
import util.PrintUtil;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static util.Alignment.*;
import static util.Alignment.CENTER;

public class TakeQuizCommand implements Command{
    private QuizRepository quizRepository;
    private QuizResultRepository quizResultRepository;
    private Scanner in;
    private PrintStream out;

    public TakeQuizCommand(QuizRepository quizRepository, QuizResultRepository quizResultRepository, Scanner in, PrintStream out) {
        this.quizRepository = quizRepository;
        this.quizResultRepository = quizResultRepository;
        this.in = in;
        this.out = out;
    }

    @Override
    public void action() {
        printQuizDashboard();
        out.printf("Take Quiz Page%nPlease enter quiz ID: %nPress enter if you want to go back to the main menu.%n");
        String input = in.nextLine().trim();
        if(input.isEmpty()){
            return;
        }
        long id = Long.parseLong(input);
        Quiz chosenQuiz = quizRepository.findById(id).orElse(null);
        if (chosenQuiz != null) {
            if(chosenQuiz.getAuthor().getId() == LoggedUser.getLoggedUser().getId()){
                out.println("You cannot take a quiz that you have created. Please choose another one.");
                this.action();
                return;
            }
            int result = 0;
            List<Question> questions = chosenQuiz.getQuestions();
            for (Question q : questions) {
                out.printf("%s%nPlease enter your answer", q.toString());
                String answer = in.nextLine().trim();
                if (q.getAnswers().stream().anyMatch(a->a.getText().equals(answer))) {
                    result += q.getAnswers().stream()
                            .filter(a -> a.getText().equals(answer)).findFirst().get().getScore();
                    out.println("Good job! This is the correct answer!");
                } else{
                    out.println("Sorry, this is the wrong answer");
                }
            }
            QuizResult quizResult = new QuizResult(LoggedUser.getLoggedUser(), chosenQuiz, result);
            try {
                //TODO: add max points for the quiz
                quizResultRepository.create(quizResult);
                out.println("Your result is " + quizResult.getScore());
            } catch (EntityAlreadyExistsException e) {
                e.getMessage();
            }
            LoggedUser.getLoggedUser().addQuizResult(quizResult);
        } else {
            out.println("There is no quiz available with the given ID.");
            this.action();
        }
    }

    private void printQuizDashboard() {
        List<PrintUtil.ColumnDescriptor> playerColumns = new ArrayList<>(List.of(
                new PrintUtil.ColumnDescriptor("id", "ID", 5, RIGHT),
                new PrintUtil.ColumnDescriptor("title", "Title", 30, CENTER),
                new PrintUtil.ColumnDescriptor("author", "Author", 20, CENTER),
                new PrintUtil.ColumnDescriptor("description", "Description", 50, CENTER),
                new PrintUtil.ColumnDescriptor("expectedDuration", "Expected Duration", 3, LEFT, 2)
        ));
        String quizReport = PrintUtil.formatTable(playerColumns,
                quizRepository.findAll(),
                "List of all quizzes");
        out.println(quizReport);
    }
}
