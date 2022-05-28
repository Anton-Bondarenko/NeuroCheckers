package sent.neuro.boardgame;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import sent.neuro.boardgame.controller.CheckerGameController;
import sent.neuro.boardgame.gui.GameWindow;
import sent.neuro.boardgame.rules.checkers.CheckersRules;

@SpringBootApplication
@Slf4j
public class NeuroBoardGamesApplication implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(NeuroBoardGamesApplication.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    @Override
    public void run(String... args) {
        log.info("EXECUTING : command line runner");
        var gameWindow = new GameWindow(new CheckerGameController(new CheckersRules()));
        gameWindow.showGameWindow();
        gameWindow.startNewGame();
    }

}
