package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.ErrorStatus;
import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.apiPayload.exception.GeneralException;
import BabAl.BabalServer.domain.User;
import BabAl.BabalServer.domain.enums.FoodCategoryName;
import BabAl.BabalServer.dto.request.SettingFoodCategoryRequestDto;
import BabAl.BabalServer.dto.request.SettingPasswordDto;
import BabAl.BabalServer.dto.request.SettingProfileRequestDto;
import BabAl.BabalServer.dto.response.SettingFoodCategoryResponseDto;
import BabAl.BabalServer.dto.response.SettingProfileResponseDto;
import BabAl.BabalServer.dto.response.SettingResponseDto;
import BabAl.BabalServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Override
    public SuccessStatus setSettingPassword(String userEmail, SettingPasswordDto dto) {

        // 해당 사용자 유무 판별
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        // 기존 비밀번호가 맞는지 판별
        String savedPassword = user.get().getPassword();
        if (!passwordEncoder.matches(dto.getPassword(), savedPassword)) {
            throw new GeneralException(ErrorStatus.PASSWORD_NOT_MATCHING);
        }

        // new password = confirm password 인지 판별
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new GeneralException(ErrorStatus.PASSWORD_CONFIRMATION_MISMATCH);
        } else {
            user.get().updatePassword(dto.getNewPassword());
            user.get().encodePassword(passwordEncoder);
            return SuccessStatus._OK;
        }
    }

    @Override
    public SettingFoodCategoryResponseDto getSettingFoodCategory(String userEmail) {

        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        } else {
            List<FoodCategoryName> foodCategoryList = user.get().getFoodCategoryNameList();
            return SettingFoodCategoryResponseDto.settingFoodCategoryResponse(foodCategoryList);
        }
    }

    @Override
    public SuccessStatus setSettingFoodCategory(String userEmail, SettingFoodCategoryRequestDto dto) {

        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        } else {
            user.get().updateFoodCategory(dto.getFoodCategory());
            return SuccessStatus._OK;
        }
    }

}
