package end2end;

import nl.jonathandegier.lingogame.LingoGame;
import nl.jonathandegier.lingogame.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LingoGame.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void test_start_game() throws Exception {
        mvc.perform(post("/api/v1/game"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.gameId", isA(Integer.class)));
    }

    @Test
    public void test_start_round() throws Exception {
        int gameId = getGameId();

        mvc.perform(post("/api/v1/game/" + gameId + "/round"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.guess", is("w")))
            .andExpect(jsonPath("$.totalGuesses", is(5)))
            .andExpect(jsonPath("$.guessesLeft", is(5)))
            .andExpect(jsonPath("$.wordLength", is(5)));
    }

    @Test
    public void test_guess_good() throws Exception {
        int gameId = getGameId();
        mvc.perform(post("/api/v1/game/" + gameId + "/round"));

        mvc.perform(post("/api/v1/game/" + gameId + "/guess")
            .content("{\"guess\": \"wordt\"}")
            .contentType(MediaType.APPLICATION_JSON))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.guess", is("wordt")))
            .andExpect(jsonPath("$.explaination", is("GOOD_GUESS")))
            .andExpect(jsonPath("$.totalGuesses", is(5)))
            .andExpect(jsonPath("$.guessesLeft", is(4)))
            .andExpect(jsonPath("$.wordLength", is(5)))

            .andExpect(jsonPath("$.feedback[0].feedbackType", is("CORRECT")))
            .andExpect(jsonPath("$.feedback[1].feedbackType", is("CORRECT")))
            .andExpect(jsonPath("$.feedback[2].feedbackType", is("PRESENT")))
            .andExpect(jsonPath("$.feedback[3].feedbackType", is("PRESENT")))
            .andExpect(jsonPath("$.feedback[4].feedbackType", is("ABSENT")));
    }

    @Test
    public void test_guess_correct() throws Exception {
        int gameId = getGameId();
        mvc.perform(post("/api/v1/game/" + gameId + "/round"));

        mvc.perform(post("/api/v1/game/" + gameId + "/guess")
            .content("{\"guess\": \"woord\"}")
            .contentType(MediaType.APPLICATION_JSON))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.guess", is("woord")))
            .andExpect(jsonPath("$.explaination", is("CORRECT")))
            .andExpect(jsonPath("$.totalGuesses", is(5)))
            .andExpect(jsonPath("$.guessesLeft", is(4)))
            .andExpect(jsonPath("$.wordLength", is(5)));
    }

    @Test
    public void test_get_score() throws Exception {
        int gameId = getGameId();
        mvc.perform(post("/api/v1/game/" + gameId + "/round"));
        mvc.perform(post("/api/v1/game/" + gameId + "/guess")
            .content("{\"guess\": \"woord\"}")
            .contentType(MediaType.APPLICATION_JSON));

        mvc.perform(get("/api/v1/game/" + gameId + "/score"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.points", is(50)));
    }

    @Test
    public void test_save_score() throws Exception {
        int gameId = getGameId();
        mvc.perform(post("/api/v1/game/" + gameId + "/round"));
        mvc.perform(post("/api/v1/game/" + gameId + "/guess")
                .content("{\"guess\": \"woord\"}")
                .contentType(MediaType.APPLICATION_JSON));

        mvc.perform(post("/api/v1/game/" + gameId + "/score")
            .content("{\"name\": \"player\"}")
            .contentType(MediaType.APPLICATION_JSON))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(200)))
            .andExpect(jsonPath("$.finalScore.points", is(50)));

        mvc.perform(post("/api/v1/game/" + gameId + "/round"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void test_end_game() throws Exception {
        int gameId = getGameId();
        mvc.perform(post("/api/v1/game/" + gameId + "/round"));
        mvc.perform(post("/api/v1/game/" + gameId + "/guess")
                .content("{\"guess\": \"woord\"}")
                .contentType(MediaType.APPLICATION_JSON));

        mvc.perform(delete("/api/v1/game/" + gameId))
            .andExpect(status().isOk());

        mvc.perform(post("/api/v1/game/" + gameId + "/round"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void test_game_is_over() throws Exception {
        int gameId = getGameId();
        mvc.perform(post("/api/v1/game/" + gameId + "/round"));

        mvc.perform(post("/api/v1/game/" + gameId + "/guess").content("{\"guess\": \"wordt\"}").contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/api/v1/game/" + gameId + "/guess").content("{\"guess\": \"wordt\"}").contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/api/v1/game/" + gameId + "/guess").content("{\"guess\": \"wordt\"}").contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/api/v1/game/" + gameId + "/guess").content("{\"guess\": \"wordt\"}").contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/api/v1/game/" + gameId + "/guess").content("{\"guess\": \"wordt\"}").contentType(MediaType.APPLICATION_JSON));

        mvc.perform(post("/api/v1/game/" + gameId + "/guess").content("{\"guess\": \"wordt\"}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotAcceptable())
            .andExpect(jsonPath("$.type", is("GAME_ALREADY_OVER")));
    }

    @Test
    public void test_round_not_started() throws Exception {
        int gameId = getGameId();

        mvc.perform(post("/api/v1/game/" + gameId + "/guess").content("{\"guess\": \"wordt\"}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotAcceptable())
            .andExpect(jsonPath("$.type", is("ROUND_NOT_STARTED")));

    }

    @Test
    public void test_uncompleted_round() throws Exception {
        int gameId = getGameId();
        mvc.perform(post("/api/v1/game/" + gameId + "/round"));

        mvc.perform(post("/api/v1/game/" + gameId + "/round"))
            .andExpect(status().isNotAcceptable())
            .andExpect(jsonPath("$.type", is("UNCOMPLETED_ROUND")));
    }

    private int getGameId() throws Exception {
        return Integer.parseInt(mvc.perform(post("/api/v1/game")).andReturn()
                .getResponse().getContentAsString().replaceAll("\\D+",""));
    }
}
