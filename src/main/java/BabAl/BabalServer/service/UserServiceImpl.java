package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.ErrorStatus;
import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.apiPayload.exception.GeneralException;
import BabAl.BabalServer.domain.User;
import BabAl.BabalServer.dto.request.LoginDto;
import BabAl.BabalServer.dto.request.SignUpDto;
import BabAl.BabalServer.dto.request.newPasswordDto;
import BabAl.BabalServer.dto.response.LoginResponseDto;
import BabAl.BabalServer.jwt.JwtUtil;
import BabAl.BabalServer.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public SuccessStatus signUp(SignUpDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new GeneralException(ErrorStatus.MEMBER_ALREADY_EXIST);
        }

        User user = userRepository.save(dto.toEntity());
        user.encodePassword(passwordEncoder);

        user.addUserAuthority();
        return SuccessStatus._OK;
    }

    public LoginResponseDto login(LoginDto dto) {
        Optional<User> savedUser = userRepository.findByEmail(dto.getEmail());

        if (savedUser.isPresent()) {
            String savedPassword = savedUser.get().getPassword();

            if (!passwordEncoder.matches(dto.getPassword(), savedPassword)) {
                throw new GeneralException(ErrorStatus.PASSWORD_NOT_MATCHING);
            }
            Long expiredMs = 1000 * 60 * 60L;
            String token = JwtUtil.createJwt(dto.getEmail(), expiredMs);

            return LoginResponseDto.loginResponse(token);
        }

        // user not found exception
        throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
    }

    public SuccessStatus sendNewPasswordEmail(newPasswordDto dto) throws Exception {

        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if (user.isPresent()) {
            String tempPassword = generateRandomPassword(10);

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(dto.getEmail());
            simpleMailMessage.setFrom(from);
            simpleMailMessage.setSubject("[BABAL] 임시 비밀번호 안내");
            simpleMailMessage.setText("임시 비밀번호 : " + tempPassword);

            try {
                user.get().updatePassword(passwordEncoder.encode(tempPassword));
                javaMailSender.send(simpleMailMessage);
            } catch (Exception e) {
                e.printStackTrace();
                throw new MessagingException("이메일 전송 실패");
            }
            return SuccessStatus._OK;
        } else {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }
    }

    public String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }

}
