package BabAl.BabalServer.apiPayload.code.status;

import BabAl.BabalServer.apiPayload.code.BaseCode;
import BabAl.BabalServer.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode  {

    _OK(HttpStatus.OK, "200", "요청이 정상 처리되었습니다")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return null;
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return null;
    }
}
