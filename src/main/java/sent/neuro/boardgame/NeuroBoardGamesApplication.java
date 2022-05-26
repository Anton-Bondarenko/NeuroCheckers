package sent.neuro.boardgame;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sent.neuro.boardgame.controller.CheckerConsoleController;
import sent.neuro.boardgame.game.Play;
import sent.neuro.boardgame.rules.CheckersRules;

@SpringBootApplication
@Slf4j
public class NeuroBoardGamesApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NeuroBoardGamesApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("EXECUTING : command line runner");

        var play = new Play(new CheckersRules(), new CheckerConsoleController());
        play.playGame();
    }

}
