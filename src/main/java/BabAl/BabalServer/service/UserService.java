package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.dto.request.LoginDto;
import BabAl.BabalServer.dto.request.SignUpDto;
import BabAl.BabalServer.dto.request.changePasswordDto;
import BabAl.BabalServer.dto.request.newPasswordDto;
import BabAl.BabalServer.dto.response.LoginResponseDto;
import jakarta.mail.MessagingException;

public interface UserService {

    SuccessStatus signUp(SignUpDto dto);
    LoginResponseDto login(LoginDto dto);
    SuccessStatus sendNewPasswordEmail(newPasswordDto dto) throws Exception;
    SuccessStatus changePassword(String email, changePasswordDto dto);
    SuccessStatus signOut(String userEmail);
}
