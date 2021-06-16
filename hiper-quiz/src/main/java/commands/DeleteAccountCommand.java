package commands;

import dao.PlayerRepository;
import exception.EntityNotFoundException;
import util.LoggedUser;

import java.io.PrintStream;
import java.util.Scanner;

public class DeleteAccountCommand implements Command{
    PlayerRepository playerRepository;
    PrintStream out;
    Scanner in;

    public DeleteAccountCommand(PlayerRepository playerRepository, Scanner in, PrintStream out) {
        this.playerRepository = playerRepository;
        this.out = out;
        this.in = in;
    }

    @Override
    public void action() {
        out.println("Are you sure you want to delete your account? Y/N");
        String input = in.nextLine();
        if(input.equals("N")){
            return;
        }
        try {
            playerRepository.deleteById(LoggedUser.getLoggedUser().getId());
            out.println("You have successfully deleted your account.");
            new LogoutCommand().action();
        }catch (EntityNotFoundException e){
            out.println(e.getMessage());
        }
    }
}
