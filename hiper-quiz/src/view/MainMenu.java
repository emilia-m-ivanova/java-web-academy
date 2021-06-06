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
import util.LoggedUser;

import java.io.*;
import java.util.*;

public class MainMenu {

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
    private final PrintStream out;
    private Player player;
    PlayerRepository userRepository = new PlayerRepositoryImpl(new LongKeyGenerator());
    QuizRepository quizRepository = new QuizRepositoryImpl(new LongKeyGenerator());
    QuizResultRepository quizResultRepository = new QuizResultRepositoryImpl(new LongKeyGenerator());

    public MainMenu(InputStream inStream, PrintStream out) {
        this.in = new Scanner(inStream);
        this.out = out;
        commandsAvailable.put("Login", new LoginCommand(userRepository,in,out));
        commandsAvailable.put("Register", new RegisterCommand(userRepository,in,out));
        commandsAvailable.put("Add quiz", new AddQuizCommand(quizRepository,in,out,player));
        commandsAvailable.put("Add questions", new AddQuestionsCommand(quizRepository,in,out,player));
        commandsAvailable.put("Take quiz", new TakeQuizCommand(quizRepository,quizResultRepository,in,out,player));
        commandsAvailable.put("Delete quiz", new DeleteQuizCommand(quizRepository,in,out,player));
        commandsAvailable.put("Show result dashboard", new ShowResultDashboardCommand(userRepository,out));
        commandsAvailable.put("Logout", new LogoutCommand());
    }
    public void start() {
        boolean finish = false;
        initializeRepositories();
        do {
            updateUser();
            out.println("           M A I N    M E N U");
            out.println("*******************************************");
            Map<Integer, String> commands;
            if (player == null) {
                commands = commandsGuest;
            } else {
                commands = commandsUser;
            }
            commands.entrySet()
                    .forEach(e -> out.println("<" + e.getKey() + "> " + e.getValue()));
            String answer;
            int chosenOption = 0;
            do {
                out.println("Please select operation [1 to " + (commands.size()) + "]:");
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

    private void initializeRepositories() {
        try {
            LoadEntitiesCommand loadCommand = new LoadEntitiesCommand(new FileInputStream("quiz.db"),userRepository,quizRepository,quizResultRepository);
            loadCommand.action();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateUser(){
        player = LoggedUser.getLoggedUser();
        ((AddQuizCommand)commandsAvailable.get("Add quiz")).updateUser(player);
        ((AddQuestionsCommand)commandsAvailable.get("Add questions")).updateUser(player);
        ((TakeQuizCommand)commandsAvailable.get("Take quiz")).updateUser(player);
        ((DeleteQuizCommand)commandsAvailable.get("Delete quiz")).updateUser(player);
    }
}
