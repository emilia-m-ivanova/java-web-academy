package commands;

import dao.PlayerRepository;
import model.Player;
import util.LoggedUser;
import java.io.PrintStream;
import java.util.Scanner;

public class LoginCommand implements Command{
    private PlayerRepository playerRepository;
    private Scanner in;
    private PrintStream out;

    public LoginCommand(PlayerRepository playerRepository, Scanner in, PrintStream out) {
        this.playerRepository = playerRepository;
        this.in = in;
        this.out = out;
    }

    @Override
    public void action() {
        out.printf("Login Page%nPress enter if you want to go back to the main menu.%nUsername: %n");
        String username = in.nextLine().trim();
        if(username.isEmpty()){
            return;
        }
        out.println("Password: ");
        String password = in.nextLine().trim();
        Player loggedInUser = playerRepository.findByUsernameAndPassword(username, password);
        if (loggedInUser != null) {
            LoggedUser.setUser(loggedInUser);
        } else {
            out.println("Wrong username or password");
        }
    }
}
