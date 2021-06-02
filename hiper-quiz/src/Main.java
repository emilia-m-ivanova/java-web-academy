import dao.KeyGenerator;
import dao.QuizRepository;
import dao.UserRepository;
import dao.impl.LongKeyGenerator;
import dao.impl.QuizRepositoryImpl;
import dao.impl.UserRepositoryImpl;
import exception.EntityAlreadyExistsException;
import static model.Gender.*;
import static util.Alignment.*;

import model.*;
import util.PrintUtil;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws EntityAlreadyExistsException {
        KeyGenerator userKeyGenerator = new LongKeyGenerator();
        Player player1 = new Player("Emilia","emilia@email.com","password",FEMALE);
        Player player2 = new Player("Valentin","valentin@email.com","password", MALE,"https://cdn.pixabay.com/photo/2017/07/22/15/21/cat-2528935_960_720.jpg","Gamer");
        UserRepository userRepository = new UserRepositoryImpl(userKeyGenerator);
        userRepository.create(player1);
        userRepository.create(player2);

        Quiz quiz = new Quiz("Java Basics",player1,"Basic questions",10);

        Question question1 = new Question("Which is the odd one?");
        Answer answer1q1 = new Answer(question1,"boolean",0);
        Answer answer2q1 = new Answer(question1,"double",0);
        Answer answer3q1 = new Answer(question1,"public",1);
        question1.setAnswers(List.of(answer1q1,answer2q1,answer3q1));

        Question question2 = new Question("What type would we use to store the number 2.5?");
        Answer answer1q2 = new Answer(question1,"double",2);
        Answer answer2q2 = new Answer(question1,"int",0);
        Answer answer3q2 = new Answer(question1,"char",0);
        question2.setAnswers(List.of(answer1q2,answer2q2,answer3q2));

        quiz.setQuestions(List.of(question1,question2));

        KeyGenerator quizKeyGenerator = new LongKeyGenerator();
        QuizRepository quizRepository = new QuizRepositoryImpl(quizKeyGenerator);
        quizRepository.create(quiz);

        QuizResult quizResult = new QuizResult(player2,quiz,2);
        player2.takeQuiz(quizResult);

        List<PrintUtil.ColumnDescriptor> metadataColumns = List.of(
                new PrintUtil.ColumnDescriptor("created", "Created", 19, CENTER),
                new PrintUtil.ColumnDescriptor("updated", "Updated", 19, CENTER)
        );

        List<PrintUtil.ColumnDescriptor> quizColumns = new ArrayList<>(List.of(
                new PrintUtil.ColumnDescriptor("id", "ID", 5, RIGHT),
                new PrintUtil.ColumnDescriptor("author", "Author", 15, CENTER),
                new PrintUtil.ColumnDescriptor("description", "Description", 30, CENTER),
                new PrintUtil.ColumnDescriptor("questions", "Questions", 80, RIGHT, 2),
                new PrintUtil.ColumnDescriptor("expectedDuration", "Duration", 10, CENTER)
        ));
        quizColumns.addAll(metadataColumns);
        String quizReport = PrintUtil.formatTable(quizColumns, quizRepository.findAll(), "Quiz Report");
        System.out.println(quizReport);

        List<PrintUtil.ColumnDescriptor> playerColumns = new ArrayList<>(List.of(
                new PrintUtil.ColumnDescriptor("id", "ID", 5, RIGHT),
                new PrintUtil.ColumnDescriptor("username", "Username", 15, CENTER),
                new PrintUtil.ColumnDescriptor("email", "Email", 30, CENTER),
                new PrintUtil.ColumnDescriptor("gender", "Gender", 10, LEFT, 2),
                new PrintUtil.ColumnDescriptor("overallScore", "OverallScore", 15, CENTER)
        ));
        playerColumns.addAll(metadataColumns);
        String playerReport = PrintUtil.formatTable(playerColumns, userRepository.findAll(), "Player Report");
        System.out.println(playerReport);
    }
}
