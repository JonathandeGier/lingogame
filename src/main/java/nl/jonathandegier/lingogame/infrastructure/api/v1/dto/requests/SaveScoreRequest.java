package nl.jonathandegier.lingogame.infrastructure.api.v1.dto.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SaveScoreRequest {

    @NotNull
    @NotBlank
    public String name;
}
