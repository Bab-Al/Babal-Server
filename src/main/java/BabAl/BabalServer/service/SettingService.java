package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.dto.request.SettingPasswordDto;
import BabAl.BabalServer.dto.request.SettingProfileRequestDto;
import BabAl.BabalServer.dto.response.SettingFoodCategoryResponseDto;
import BabAl.BabalServer.dto.response.SettingProfileResponseDto;
import BabAl.BabalServer.dto.response.SettingResponseDto;

public interface SettingService {

    // 마이페이지 조회
    SettingResponseDto getSetting(String userEmail);

    // 마이페이지 Edit Profile 조회
    SettingProfileResponseDto getSettingProfile(String userEmail);

    // 마이페이지 Edit Profile 수정
    SuccessStatus setSettingProfile(String userEmail, SettingProfileRequestDto dto);

    // 마이페이지 비밀번호 재설정
    SuccessStatus setSettingPassword(String userEmail, SettingPasswordDto dto);

    // 마이페이지 Edit Food Category 조회
    SettingFoodCategoryResponseDto getSettingFoodCategory(String userEmail);

}
