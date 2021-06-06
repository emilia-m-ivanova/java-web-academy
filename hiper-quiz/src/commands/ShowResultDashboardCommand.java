package commands;

import dao.PlayerRepository;
import model.Player;
import util.PrintUtil;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static util.Alignment.*;
import static util.Alignment.CENTER;

public class ShowResultDashboardCommand implements Command{
    private PlayerRepository playerRepository;
    private PrintStream out;

    public ShowResultDashboardCommand(PlayerRepository playerRepository, PrintStream out) {
        this.playerRepository = playerRepository;
        this.out = out;
    }

    @Override
    public void action() {
        List<PrintUtil.ColumnDescriptor> playerColumns = new ArrayList<>(List.of(
                new PrintUtil.ColumnDescriptor("id", "ID", 5, RIGHT),
                new PrintUtil.ColumnDescriptor("username", "Username", 15, CENTER),
                new PrintUtil.ColumnDescriptor("email", "Email", 30, CENTER),
                new PrintUtil.ColumnDescriptor("gender", "Gender", 10, LEFT, 2),
                new PrintUtil.ColumnDescriptor("overallScore", "OverallScore", 15, CENTER)
        ));
        String playerReport = PrintUtil.formatTable(playerColumns,
                playerRepository.findAll().stream()
                        .sorted(Comparator.comparingInt(Player::getOverallScore).reversed())
                        .limit(5).collect(Collectors.toList()),
                "Player Report");
        out.println(playerReport);
    }
}
