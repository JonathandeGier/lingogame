package nl.jonathandegier.lingogame.infrastructure.api.v1.dto.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class GuessRequest {

    @NotNull
    @NotBlank
    public String guess;
}
