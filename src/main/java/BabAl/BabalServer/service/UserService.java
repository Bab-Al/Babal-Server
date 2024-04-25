package BabAl.BabalServer.service;

import BabAl.BabalServer.dto.request.LoginDto;
import BabAl.BabalServer.dto.request.SignInDto;

public interface UserService {

    Long signIn(SignInDto dto);
    String login(LoginDto dto);
}
