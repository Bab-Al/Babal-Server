package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.dto.request.SettingProfileRequestDto;
import BabAl.BabalServer.dto.response.SettingProfileResponseDto;
import BabAl.BabalServer.dto.response.SettingResponseDto;

public interface SettingService {

    // 마이페이지 조회
    SettingResponseDto getSetting(String userEmail);

    // 마이페이지 Edit Profile 조회
    SettingProfileResponseDto getSettingProfile(String userEmail);

    // 마이페이지 Edit Profile 수정
    SuccessStatus setSettingProfile(String userEmail, SettingProfileRequestDto dto);

}
