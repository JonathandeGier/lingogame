package nl.jonathandegier.lingogame.infrastructure.webapp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Webcontroller Test")
public class WebControllerTest {

    @Test
    @DisplayName("Test get homepage")
    void test_get_homepage() {
        var controller = new WebController();
        assertEquals("webapp/index.html", controller.homePage());
    }

    @Test
    @DisplayName("Test get game page")
    void test_get_play_page() {
        var controller = new WebController();
        assertEquals("webapp/game.html", controller.playGame());
    }

    @Test
    @DisplayName("Test get highscores page")
    void test_get_highscores_page() {
        var controller = new WebController();
        assertEquals("webapp/highscores.html", controller.highscores());
    }
}
