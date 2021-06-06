package commands;

import dao.QuizRepository;
import model.Answer;
import model.Player;
import model.Question;
import model.Quiz;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AddQuestionsCommand implements Command{
    private QuizRepository quizRepository;
    private Scanner in;
    private Player player;
    private long quizID = 0;

    public AddQuestionsCommand(QuizRepository quizRepository, Scanner in, Player player) {
        this.quizRepository = quizRepository;
        this.in = in;
        this.player = player;
    }

    public AddQuestionsCommand(QuizRepository quizRepository, Scanner in, Player player, long quizID) {
        this.quizRepository = quizRepository;
        this.in = in;
        this.player = player;
        this.quizID = quizID;
    }

    @Override
    public void action() {
        System.out.println("Add Questions Page");
        if(quizID==0){
            System.out.println("Please enter quiz ID:");
            quizID = Long.parseLong(in.nextLine().trim());
        }
        Quiz chosenQuiz = quizRepository.findByIdAndAuthor(quizID, player);
        if (chosenQuiz != null) {
            System.out.println("Please enter your question");
            String questionString = in.nextLine().trim();
            Question question = new Question(questionString);
            System.out.println("Please enter the correct answers, separated by coma");
            String[] answerStrings = in.nextLine().split("//s+,//s+");
            System.out.println("Please enter the points given for correct answer(s)");
            int points = Integer.parseInt(in.nextLine().trim());
            List<Answer> answerList = Arrays.stream(answerStrings)
                    .map(a -> new Answer(question, a, points))
                    .collect(Collectors.toList());
            question.setAnswers(answerList);
            chosenQuiz.addQuestion(question);
            addMoreQuestions(chosenQuiz);
        } else {
            System.out.println("You haven't created a quiz with the given ID.");
            this.action();
        }
    }

    private void addMoreQuestions(Quiz chosenQuiz) {
        while (true){
            System.out.println("Would you like to add another question to quiz " + chosenQuiz.getTitle() + "? Y/N");
            String confirmation = in.nextLine().trim();
            if(confirmation.equals("Y")){
                this.action();
            }else if(confirmation.equals("N")){
                break;
            }else{
                System.out.println("Please enter Y for \"yes\" or N for \"no\"");
            }
        }
    }
}
