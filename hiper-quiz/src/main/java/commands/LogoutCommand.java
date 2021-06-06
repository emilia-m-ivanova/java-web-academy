package commands;

import util.LoggedUser;

public class LogoutCommand implements Command{
    @Override
    public void action() {
        LoggedUser.setUser(null);
    }
}
