package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.dto.request.LoginDto;
import BabAl.BabalServer.dto.request.SignUpDto;
import BabAl.BabalServer.dto.request.ChangePasswordDto;
import BabAl.BabalServer.dto.request.NewPasswordDto;
import BabAl.BabalServer.dto.response.LoginResponseDto;

public interface UserService {

    SuccessStatus signUp(SignUpDto dto);
    LoginResponseDto login(LoginDto dto);
    SuccessStatus sendNewPasswordEmail(NewPasswordDto dto) throws Exception;
    SuccessStatus changePassword(String email, ChangePasswordDto dto);
    SuccessStatus signOut(String userEmail);
}
