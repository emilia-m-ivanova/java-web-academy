package view;

import commands.*;
import dao.QuizRepository;
import dao.QuizResultRepository;
import dao.PlayerRepository;
import dao.impl.*;
import exception.EntityAlreadyExistsException;
import model.Gender;
import model.Player;
import util.LoggedUser;

import java.io.*;
import java.util.*;

public class MainMenu {

    private static final Map<Integer, String> commandsGuest = new LinkedHashMap<>();
    private static final Map<Integer, String> commandsUser = new LinkedHashMap<>();
    private static final Map<String, Command> commandsAvailable = new HashMap<>();
    private final Scanner in;
    private final PrintStream out;
    private PlayerRepository userRepository = new PlayerRepositoryJpaImpl();
    private QuizRepository quizRepository = new QuizRepositoryImpl(new LongKeyGenerator());
    private QuizResultRepository quizResultRepository = new QuizResultRepositoryImpl(new LongKeyGenerator());

    public MainMenu(InputStream inStream, PrintStream out) {
        this.in = new Scanner(inStream);
        this.out = out;

        commandsAvailable.put("Login", new LoginCommand(userRepository,in,out));
        commandsAvailable.put("Register", new RegisterCommand(userRepository,in,out));
        commandsAvailable.put("Add quiz", new AddQuizCommand(quizRepository,in,out));
        commandsAvailable.put("Add questions", new AddQuestionsCommand(quizRepository,in,out));
        commandsAvailable.put("Take quiz", new TakeQuizCommand(quizRepository,quizResultRepository,in,out));
        commandsAvailable.put("Delete quiz", new DeleteQuizCommand(quizRepository,in,out));
        commandsAvailable.put("Show result dashboard", new ShowResultDashboardCommand(userRepository,out));
        commandsAvailable.put("Change password", new ChangePasswordCommand(userRepository,in,out));
        commandsAvailable.put("Delete Account", new DeleteAccountCommand(userRepository,in,out));
        commandsAvailable.put("Logout", new LogoutCommand());

        commandsUser.put(1, "Add quiz");
        commandsUser.put(2, "Add questions");
        commandsUser.put(3, "Take quiz");
        commandsUser.put(4, "Delete quiz");
        commandsUser.put(5, "Show result dashboard");
        commandsUser.put(6, "Logout");
        commandsUser.put(7, "Change password");
        commandsUser.put(8, "Delete Account");
        commandsUser.put(9, "Exit menu");

        commandsGuest.put(1, "Login");
        commandsGuest.put(2, "Register");
        commandsGuest.put(3, "Exit menu");
        ((PlayerRepositoryJpaImpl)userRepository).init();
    }
    public void start() {
        boolean finish = false;
        //initializeRepositories();
        do {
//            try {
//                userRepository.createFromMemory(List.of(
//                        new Player("valio","valio@valio.com","123werT*", Gender.MALE),
//                        new Player("daniel","dani@gmail.com","Daniel1*", Gender.MALE),
//                        new Player("lili","lili@gmail.com","Lily1234%", Gender.FEMALE)
//                ));
//            } catch (EntityAlreadyExistsException e) {
//                e.printStackTrace();
//            }
            out.println("           M A I N    M E N U");
            out.println("*******************************************");
            Map<Integer, String> commands;
            if (LoggedUser.getLoggedUser() == null) {
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
//                try {
//                    SaveEntitiesCommand saveCommand = new SaveEntitiesCommand(new FileOutputStream("quiz.db"),
//                            userRepository,quizRepository,quizResultRepository);
//                    saveCommand.action();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
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
}
