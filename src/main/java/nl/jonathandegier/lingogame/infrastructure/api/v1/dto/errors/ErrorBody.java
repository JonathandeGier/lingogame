package nl.jonathandegier.lingogame.infrastructure.api.v1.dto.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorBody {

    private int statusInt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    public LocalDateTime timestamp;

    private String message;
    private ErrorType type;

    public ErrorBody(HttpStatus httpStatus, String message, ErrorType type) {
        this.statusInt = httpStatus.value();
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.type = type;
    }


    public int getStatusInt() {
        return statusInt;
    }

    public String getMessage() {
        return message;
    }

    public ErrorType getType() {
        return type;
    }
}
