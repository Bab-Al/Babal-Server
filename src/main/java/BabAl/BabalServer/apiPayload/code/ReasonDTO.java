package BabAl.BabalServer.apiPayload.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ReasonDTO implements BaseCode {

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    private final boolean isSuccess;

    @Override
    public ReasonDTO getReason() {
        return null;
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return null;
    }
}
