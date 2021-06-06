package commands;

import dao.PlayerRepository;
import model.Player;
import view.LoggedUser;

import java.util.Scanner;

public class LoginCommand implements Command{
    private PlayerRepository playerRepository;
    private Scanner in;

    public LoginCommand(PlayerRepository playerRepository, Scanner in) {
        this.playerRepository = playerRepository;
        this.in = in;
    }

    @Override
    public void action() {
        System.out.printf("Login Page%nUsername: %n");
        String username = in.nextLine().trim();
        System.out.println("Password: ");
        String password = in.nextLine().trim();
        Player loggedInUser = playerRepository.findByUsernameAndPassword(username, password);
        if (loggedInUser != null) {
            LoggedUser.setUser(loggedInUser);
        } else {
            System.out.println("Wrong username or password");
        }
    }
}
