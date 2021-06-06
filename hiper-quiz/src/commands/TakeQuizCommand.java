package commands;

import dao.QuizRepository;
import dao.QuizResultRepository;
import exception.EntityAlreadyExistsException;
import model.Player;
import model.Question;
import model.Quiz;
import model.QuizResult;
import util.PrintUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static util.Alignment.*;
import static util.Alignment.CENTER;

public class TakeQuizCommand implements Command{
    private QuizRepository quizRepository;
    private QuizResultRepository quizResultRepository;
    private Scanner in;
    private Player player;

    public TakeQuizCommand(QuizRepository quizRepository, QuizResultRepository quizResultRepository, Scanner in, Player player) {
        this.quizRepository = quizRepository;
        this.quizResultRepository = quizResultRepository;
        this.in = in;
        this.player = player;
    }

    @Override
    public void action() {
        printQuizDashboard();
        System.out.printf("Take Quiz Page%nPlease enter quiz ID: %n");
        long id = Long.parseLong(in.nextLine().trim());
        Quiz chosenQuiz = quizRepository.findById(id).orElse(null);
        if (chosenQuiz != null) {
            if(chosenQuiz.getAuthor().getId() == player.getId()){
                System.out.println("You cannot take a quiz that you have created. Please choose another one.");
                this.action();
            }
            int result = 0;
            List<Question> questions = chosenQuiz.getQuestions();
            for (Question q : questions) {
                System.out.printf("%s%nPlease enter your answer", q.toString());
                String answer = in.nextLine().trim();
                if (q.getAnswers().stream().anyMatch(a->a.getText().equals(answer))) {
                    result += q.getAnswers().stream()
                            .filter(a -> a.getText().equals(answer)).findFirst().get().getScore();
                }
            }
            QuizResult quizResult = new QuizResult(player, chosenQuiz, result);
            try {
                quizResultRepository.create(quizResult);
            } catch (EntityAlreadyExistsException e) {
                e.getMessage();
            }
            player.addQuizResult(quizResult);

        } else {
            System.out.println("There is no quiz available with the given ID.");
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
        System.out.println(quizReport);
    }
}
