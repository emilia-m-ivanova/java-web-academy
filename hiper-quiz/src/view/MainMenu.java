package view;

import commands.*;
import dao.QuizRepository;
import dao.QuizResultRepository;
import dao.PlayerRepository;
import dao.impl.LongKeyGenerator;
import dao.impl.QuizRepositoryImpl;
import dao.impl.QuizResultRepositoryImpl;
import dao.impl.PlayerRepositoryImpl;
import model.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

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
            5, "Show result dashboard",
            6, "Logout",
            7, "Exit menu"

    );
    private static final Map<String, Command> commandsAvailable = new HashMap<>();
    private final Scanner in;
    private Player player;
    PlayerRepository userRepository = new PlayerRepositoryImpl(new LongKeyGenerator());
    QuizRepository quizRepository = new QuizRepositoryImpl(new LongKeyGenerator());
    QuizResultRepository quizResultRepository = new QuizResultRepositoryImpl(new LongKeyGenerator());

    public MainMenu(InputStream inStream) {
        this.in = new Scanner(inStream);
    }
    public void start() {
        boolean finish = false;
        try {
            LoadEntitiesCommand loadCommand = new LoadEntitiesCommand(new FileInputStream("quiz.db"),userRepository,quizRepository,quizResultRepository);
            loadCommand.action();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        do {
            player = LoggedUser.getLoggedUser();
            updateCommands();
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
                try {
                    SaveEntitiesCommand saveCommand = new SaveEntitiesCommand(new FileOutputStream("quiz.db"),
                            userRepository,quizRepository,quizResultRepository);
                    saveCommand.action();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                finish = true;
            }

            Command commandToExecute = commandsAvailable.get(commands.get(chosenOption));
            if (commandToExecute != null) {
                commandToExecute.action();
            }
            System.out.println("\n");
        } while (!finish);
    }
    private void updateCommands(){
        commandsAvailable.put("Login", new LoginCommand(userRepository,in));
        commandsAvailable.put("Register", new RegisterCommand(userRepository,in));
        commandsAvailable.put("Add quiz", new AddQuizCommand(quizRepository,in,player));
        commandsAvailable.put("Add questions", new AddQuestionsCommand(quizRepository,in,player));
        commandsAvailable.put("Take quiz", new TakeQuizCommand(quizRepository,quizResultRepository,in,player));
        commandsAvailable.put("Delete quiz", new DeleteQuizCommand(quizRepository,in,player));
        commandsAvailable.put("Show result dashboard", new ShowResultDashboardCommand(userRepository));
        commandsAvailable.put("Logout", new LogoutCommand());
    }
}
