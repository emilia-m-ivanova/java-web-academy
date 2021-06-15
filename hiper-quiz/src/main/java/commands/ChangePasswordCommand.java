package commands;

import dao.PlayerRepository;
import exception.EntityNotFoundException;
import model.Player;
import util.InputUtils;
import util.LoggedUser;

import java.io.PrintStream;
import java.util.Scanner;

public class ChangePasswordCommand implements Command {

    PlayerRepository playerRepository;
    PrintStream out;
    Scanner in;

    public ChangePasswordCommand(PlayerRepository playerRepository, Scanner in, PrintStream out) {
        this.playerRepository = playerRepository;
        this.out = out;
        this.in = in;
    }

    @Override
    public void action() {
        out.printf("Change Password Page%nPress enter if you want to go back to the main menu.%nOld password: %n");
        String username = LoggedUser.getLoggedUser().getUsername();
        String password = in.nextLine().trim();
        if(password.isEmpty()){
            return;
        }
        Player loggedInUser = playerRepository.findByUsernameAndPassword(username, password);
        if (loggedInUser != null) {
            out.println("Please enter your new password");
            String newPassword = in.nextLine().trim();
            while (!InputUtils.passwordValidator(newPassword)) {
                out.println("Password doesn\'t meet the requirements: 8 to 15 characters long, at least one digit, one capital letter, and one special sign");
                newPassword = in.nextLine().trim();
            }
            loggedInUser.setPassword(newPassword);
            try {
                playerRepository.update(loggedInUser);
            } catch (EntityNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {
            out.println("Wrong password");
        }
    }
}
