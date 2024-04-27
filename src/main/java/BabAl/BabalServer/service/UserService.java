package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.dto.request.LoginDto;
import BabAl.BabalServer.dto.request.SignUpDto;
import BabAl.BabalServer.dto.response.LoginResponseDto;

public interface UserService {

    SuccessStatus signUp(SignUpDto dto);
    LoginResponseDto login(LoginDto dto);
}
