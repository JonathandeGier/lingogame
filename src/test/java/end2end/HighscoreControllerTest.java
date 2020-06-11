package end2end;

import nl.jonathandegier.lingogame.LingoGame;
import nl.jonathandegier.lingogame.application.HighScoreService;
import nl.jonathandegier.lingogame.domain.score.Score;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = LingoGame.class)
@AutoConfigureMockMvc
public class HighscoreControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private HighScoreService highScoreService;

    @Test
    public void test_get_scores() throws Exception {

        // given 2 scores in the database
        highScoreService.saveScore(new Score("player1", 100));
        highScoreService.saveScore(new Score("player2", 200));

        // when getting the scores
        mvc.perform(get("/api/v1/scores"))

        // expect the scores in the right order
            .andExpect(jsonPath("$[0].player", is("player2")))
            .andExpect(jsonPath("$[0].points", is(200)))
            .andExpect(jsonPath("$[1].player", is("player1")))
            .andExpect(jsonPath("$[1].points", is(100)));
    }
}
