package BabAl.BabalServer.apiPayload.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorReasonDTO implements BaseErrorCode {
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    private final boolean isSuccess;

    @Override
    public ErrorReasonDTO getReason() {
        return null;
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return null;
    }
}
