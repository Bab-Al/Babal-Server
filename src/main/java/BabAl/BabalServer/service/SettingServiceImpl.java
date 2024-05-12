package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.ErrorStatus;
import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.apiPayload.exception.GeneralException;
import BabAl.BabalServer.domain.User;
import BabAl.BabalServer.dto.request.SettingProfileRequestDto;
import BabAl.BabalServer.dto.response.SettingProfileResponseDto;
import BabAl.BabalServer.dto.response.SettingResponseDto;
import BabAl.BabalServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SettingResponseDto getSetting(String userEmail) {

        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        return SettingResponseDto.settingResponse(user.get());
    }

    @Override
    public SettingProfileResponseDto getSettingProfile(String userEmail) {

        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        return SettingProfileResponseDto.settingProfileResponse(user.get());
    }

    @Override
    public SuccessStatus setSettingProfile(String userEmail, SettingProfileRequestDto dto) {

        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            user.get().updateProfile(dto);
            return SuccessStatus._OK;
        } else {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }
    }
}
