package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.dto.request.LoginDto;
import BabAl.BabalServer.dto.request.SignInDto;

public interface UserService {

    SuccessStatus signIn(SignInDto dto);
    String login(LoginDto dto);
}
