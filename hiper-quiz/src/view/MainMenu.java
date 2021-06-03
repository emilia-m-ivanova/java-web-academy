package view;

import dao.QuizRepository;
import dao.QuizResultRepository;
import dao.PlayerRepository;
import dao.impl.LongKeyGenerator;
import dao.impl.QuizRepositoryImpl;
import dao.impl.QuizResultRepositoryImpl;
import dao.impl.PlayerRepositoryImpl;
import exception.EntityAlreadyExistsException;

import exception.EntityNotFoundException;
import model.*;
import util.PrintUtil;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static util.Alignment.*;

public class MainMenu {

    public static void main(String[] args) {
        MainMenu menu = new MainMenu(System.in);
        menu.start();
    }

    private static final Map<Integer, String> commandsGuest = Map.of(
            1, "Login",
            2, "Register",
            3, "Exit menu"

    );
    private static final Map<Integer, String> commandsUser = Map.of(
            1, "Add quiz",
            2, "Add questions",
            3, "Take quiz",
            4, "Delete quiz",
            5, "Show dashboard",
            6, "Logout",
            7, "Exit menu"

    );
    private static final Map<String, Command> commandsAvailable = new HashMap<>();
    private final Scanner in;
    private Player player;
    PlayerRepository userRepository = new PlayerRepositoryImpl(new LongKeyGenerator());
    QuizRepository quizRepository = new QuizRepositoryImpl(new LongKeyGenerator());
    QuizResultRepository quizResultRepository = new QuizResultRepositoryImpl(new LongKeyGenerator());

    public void start() {
        boolean finish = false;
        do {
            System.out.println("           M A I N    M E N U");
            System.out.println("*******************************************");
            Map<Integer, String> commands;
            if (player == null) {
                commands = commandsGuest;
            } else {
                commands = commandsUser;
            }
            commands.entrySet()
                    .forEach(e -> System.out.println("<" + e.getKey() + "> " + e.getValue()));
            String answer;
            int chosenOption = 0;
            do {
                System.out.println("Please select operation [1 to " + (commands.size()) + "]:");
                answer = in.nextLine();
                try {
                    chosenOption = Integer.parseInt(answer);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid option - should be [1 to " + (commands.size()) + "].");
                }
            } while (chosenOption <= 0 || chosenOption > commands.size());
            if (commands.get(chosenOption).equals("Exit menu")) {
                finish = true;
            }

            Command commandToExecute = commandsAvailable.get(commands.get(chosenOption));
            if (commandToExecute != null) {
                commandToExecute.action();
            }
            System.out.println("\n");
        } while (!finish);
    }

    public MainMenu(InputStream inStream) {
        this.in = new Scanner(inStream);
        commandsAvailable.put("Login", new Command() {
            @Override
            public void action() {
                System.out.printf("Login Page%nUsername: %n");
                String username = in.nextLine().trim();
                System.out.println("Password: ");
                String password = in.nextLine().trim();
                Player loggedInUser = userRepository.findByUsernameAndPassword(username, password);
                if (loggedInUser != null) {
                    player = loggedInUser;
                } else {
                    System.out.println("Wrong username or password");
                }
            }
        });
        commandsAvailable.put("Register", new Command() {
            @Override
            public void action() {
                System.out.printf("Register Page%nUsername: %n");
                String username = in.nextLine().trim();
                System.out.println("Email: ");
                String email = in.nextLine().trim();
                System.out.println("Password: ");
                String password = in.nextLine().trim();
                System.out.println("Gender: ");
                String gender = in.nextLine().trim();
                try {
                    userRepository.create(new Player(username, email, password, Gender.valueOf(gender)));
                    commandsAvailable.get("Login").action();
                } catch (EntityAlreadyExistsException e) {
                    e.getMessage();
                }
            }
        });
        commandsAvailable.put("Add quiz", new Command() {
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
                    quizRepository.create(new Quiz(title, player, description, expectedDuration));
                } catch (EntityAlreadyExistsException e) {
                    e.getMessage();
                }
            }
        });
        commandsAvailable.put("Add questions", new Command() {
            @Override
            public void action() {
                System.out.printf("Add Quiz Page%nPlease enter quiz ID: %n");
                long id = Long.parseLong(in.nextLine().trim());
                Quiz chosenQuiz = quizRepository.findByIdAndAuthor(id, player);
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
                } else {
                    System.out.println("You haven't created a quiz with the given ID.");
                    commandsAvailable.get("Add questions").action();
                }
            }
        });
        commandsAvailable.put("Take quiz", new Command() {
            @Override
            public void action() {
                System.out.printf("Take Quiz Page%nPlease enter quiz ID: %n");
                long id = Long.parseLong(in.nextLine().trim());
                Quiz chosenQuiz = quizRepository.findById(id).orElse(null);
                if (chosenQuiz != null) {
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
                    commandsAvailable.get("Take quiz").action();
                }
            }
        });
        commandsAvailable.put("Delete quiz", new Command() {
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
                    commandsAvailable.get("Delete questions").action();
                }
            }
        });
        commandsAvailable.put("Show dashboard", new Command() {
            @Override
            public void action() {
                List<PrintUtil.ColumnDescriptor> playerColumns = new ArrayList<>(List.of(
                        new PrintUtil.ColumnDescriptor("id", "ID", 5, RIGHT),
                        new PrintUtil.ColumnDescriptor("username", "Username", 15, CENTER),
                        new PrintUtil.ColumnDescriptor("email", "Email", 30, CENTER),
                        new PrintUtil.ColumnDescriptor("gender", "Gender", 10, LEFT, 2),
                        new PrintUtil.ColumnDescriptor("overallScore", "OverallScore", 15, CENTER)
                ));
                String playerReport = PrintUtil.formatTable(playerColumns,
                        userRepository.findAll().stream()
                                .sorted(Comparator.comparingInt(Player::getOverallScore).reversed())
                                .limit(5).collect(Collectors.toList()),
                        "Player Report");
                System.out.println(playerReport);
            }
        });
        commandsAvailable.put("Logout", new Command() {
            @Override
            public void action() {
                player = null;
            }
        });
    }
}
