package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.ErrorStatus;
import BabAl.BabalServer.apiPayload.exception.GeneralException;
import BabAl.BabalServer.domain.User;
import BabAl.BabalServer.dto.request.LoginDto;
import BabAl.BabalServer.dto.request.SignInDto;
import BabAl.BabalServer.jwt.JwtUtil;
import BabAl.BabalServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long signIn(SignInDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new GeneralException(ErrorStatus.MEMBER_ALREADY_EXIST);
        }

        User user = userRepository.save(dto.toEntity());
        user.encodePassword(passwordEncoder);

        user.addUserAuthority();
        return user.getId();
    }

    public String login(LoginDto dto) {
        Optional<User> savedUser = userRepository.findByEmail(dto.getEmail());

        if (savedUser.isPresent()) {
            String savedPassword = savedUser.get().getPassword();

            if (!passwordEncoder.matches(dto.getPassword(), savedPassword)) {
                throw new GeneralException(ErrorStatus.PASSWORD_NOT_MATCHING);
            }
            Long expiredMs = 1000 * 60 * 60L;
            return JwtUtil.createJwt(dto.getEmail(), expiredMs);
        }

        // user not found exception
        return String.valueOf(new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

}
