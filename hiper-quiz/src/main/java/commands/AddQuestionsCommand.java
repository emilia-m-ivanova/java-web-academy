package commands;

import dao.QuizRepository;
import model.Answer;

import model.Question;
import model.Quiz;
import util.LoggedUser;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AddQuestionsCommand implements Command{
    private QuizRepository quizRepository;
    private Scanner in;
    private long quizID = 0;
    private PrintStream out;

    public AddQuestionsCommand(QuizRepository quizRepository, Scanner in, PrintStream out) {
        this.quizRepository = quizRepository;
        this.in = in;
        this.out = out;
    }

    public AddQuestionsCommand(QuizRepository quizRepository, Scanner in, PrintStream out, long quizID) {
        this.quizRepository = quizRepository;
        this.in = in;
        this.out = out;
        this.quizID = quizID;
    }

    @Override
    public void action() {
        out.println("Add Questions Page");
        if(quizID==0){
            out.printf("Please enter quiz ID:%nPress enter if you want to go back to the main menu.%n");
            String input = in.nextLine().trim();
            if(input.isEmpty()){
                return;
            }
            quizID = Long.parseLong(input);
        }
        Quiz chosenQuiz = quizRepository.findByIdAndAuthor(quizID, LoggedUser.getLoggedUser());
        if (chosenQuiz != null) {
            out.println("Please enter your question");
            String questionString = in.nextLine().trim();
            Question question = new Question(questionString);
            out.println("Please enter the correct answers, separated by coma");
            String[] answerStrings = in.nextLine().split("//s+,//s+");
            out.println("Please enter the points given for correct answer(s)");
            int points = Integer.parseInt(in.nextLine().trim());
            List<Answer> answerList = Arrays.stream(answerStrings)
                    .map(a -> new Answer(question, a, points))
                    .collect(Collectors.toList());
            question.setAnswers(answerList);
            chosenQuiz.addQuestion(question);
            while (true){
                out.println("Would you like to add another question to quiz " + chosenQuiz.getTitle() + "? Y/N");
                String confirmation = in.nextLine().trim();
                if(confirmation.equals("Y")){
                    this.action();
                    return;
                }else if(confirmation.equals("N")){
                    break;
                }else{
                    out.println("Please enter Y for \"yes\" or N for \"no\"");
                }
            }
        } else {
            out.println("You haven't created a quiz with the given ID.");
            quizID=0;
            this.action();
        }
    }

}
