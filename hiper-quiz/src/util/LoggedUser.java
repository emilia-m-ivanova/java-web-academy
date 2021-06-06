package util;

import model.Player;

public class LoggedUser {
    private Player player;

    private LoggedUser() {
    }

    private static LoggedUser loggedUser = new LoggedUser();

    public static Player getLoggedUser() {
        return loggedUser.player;
    }

    public static void setUser(Player player){
        loggedUser.player = player;
    }
}
