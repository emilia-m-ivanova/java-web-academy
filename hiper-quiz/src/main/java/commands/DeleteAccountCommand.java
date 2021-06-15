package commands;

import dao.PlayerRepository;
import exception.EntityNotFoundException;
import util.LoggedUser;

import java.io.PrintStream;

public class DeleteAccountCommand implements Command{
    PlayerRepository playerRepository;
    PrintStream out;

    public DeleteAccountCommand(PlayerRepository playerRepository, PrintStream out) {
        this.playerRepository = playerRepository;
        this.out = out;
    }

    @Override
    public void action() {
        try {
            playerRepository.deleteById(LoggedUser.getLoggedUser().getId());
            out.println("You have successfully deleted your account.");
            new LogoutCommand().action();
        }catch (EntityNotFoundException e){
            out.println(e.getMessage());
        }
    }
}
