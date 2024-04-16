package BabAl.BabalServer.service;

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

    public Long signIn(SignInDto dto) throws Exception {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다");
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
                throw new IllegalArgumentException("잘못된 비밀번호 입니다");
            }
            Long expiredMs = 1000 * 60 * 60L;
            return JwtUtil.createJwt(dto.getEmail(), expiredMs);
        }

        // user not found exception
        return String.valueOf(new IllegalArgumentException("가입되지 않은 이메일입니다"));
    }

}
