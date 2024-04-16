package BabAl.BabalServer.service;

import BabAl.BabalServer.domain.User;
import BabAl.BabalServer.jwt.JwtUtil;
import BabAl.BabalServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String login(String email, String password) {
        User savedUser = userRepository.findByEmail(email);

        if (savedUser != null) {
            String savedPassword = savedUser.getPassword();

            Long expiredMs = 1000 * 60 * 60L;
            return JwtUtil.createJwt(email, expiredMs);
//            if (bCryptPasswordEncoder.matches(password, savedPassword)) {
//                Long expiredMs = 1000 * 60 * 60L;
//                return JwtUtil.createJwt(email, expiredMs);
//            }
        }

        // user not found exception
        return "user not found";
    }

}
