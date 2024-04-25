package BabAl.BabalServer.apiPayload.exception.handler;

import BabAl.BabalServer.apiPayload.code.BaseErrorCode;
import BabAl.BabalServer.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
