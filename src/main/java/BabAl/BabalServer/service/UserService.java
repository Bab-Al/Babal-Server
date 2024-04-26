package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.dto.request.LoginDto;
import BabAl.BabalServer.dto.request.SignUpDto;

public interface UserService {

    SuccessStatus signUp(SignUpDto dto);
    String login(LoginDto dto);
}
