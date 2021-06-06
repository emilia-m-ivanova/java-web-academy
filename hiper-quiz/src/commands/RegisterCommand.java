package commands;

import dao.PlayerRepository;
import exception.EntityAlreadyExistsException;
import model.Gender;
import model.Player;
import util.InputUtils;

import java.util.Scanner;

public class RegisterCommand implements Command{
    private PlayerRepository playerRepository;
    private Scanner in;

    public RegisterCommand(PlayerRepository playerRepository, Scanner in) {
        this.playerRepository = playerRepository;
        this.in = in;
    }

    @Override
    public void action() {
        System.out.printf("Register Page%nUsername: %n");
        String username = in.nextLine().trim();
        while (!InputUtils.usernameValidator(username)){
            System.out.println("Username should be 2 to 15 characters long, word characters only. Please enter a valid username");
            username = in.nextLine().trim();
        }
        System.out.println("Email: ");
        String email = in.nextLine().trim();
        while (!InputUtils.emailValidator(email)){
            System.out.println("Please enter a valid email");
            email = in.nextLine().trim();
        }
        System.out.println("Password: ");
        String password = in.nextLine().trim();
        //TODO: add password validation
        System.out.println("Gender: ");
        String gender = in.nextLine().trim();
        while (!InputUtils.genderValidator(gender.toUpperCase())){
            //TODO: fix gender validation
//                    System.out.println("Gender is not valid");
//                    email = in.nextLine().trim();
        }
        try {
            playerRepository.create(new Player(username, email, password, Gender.valueOf(gender.toUpperCase())));
            new LoginCommand(playerRepository,in).action();
        } catch (EntityAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }
}
