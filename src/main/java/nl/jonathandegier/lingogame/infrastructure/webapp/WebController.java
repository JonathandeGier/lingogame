package nl.jonathandegier.lingogame.infrastructure.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String homePage() {
        return "webapp/index.html";
    }

    @GetMapping("/play")
    public String playGame() {
        return "webapp/game.html";
    }

    @GetMapping("/highscores")
    public String highscores() {
        return "webapp/highscores";
    }
}
