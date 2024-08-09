package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.ErrorStatus;
import BabAl.BabalServer.apiPayload.exception.GeneralException;
import BabAl.BabalServer.domain.User;
import BabAl.BabalServer.dto.request.FlaskMenuRecommendationDto;
import BabAl.BabalServer.dto.response.MenuRecommendationResponseDto;
import BabAl.BabalServer.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper; // 데이터를 JSON 객체로 변환

    @Value("${flask.url}")
    private String flaskUrl;

    @Override
    public List<MenuRecommendationResponseDto> recommendMenuList(String userEmail) throws JsonProcessingException {

        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        // flask 로 전송할 사용자 데이터 조회 + dto 세팅
        FlaskMenuRecommendationDto flaskRequestDto = FlaskMenuRecommendationDto.flaskRequestDto(user.get());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(flaskRequestDto), httpHeaders);

        // 응답
        String flaskResponse = restTemplate.postForObject(flaskUrl, entity, String.class);

        // JSON 응답을 MenuRecommendationResponseDto 객체 배열로 변환
        MenuRecommendationResponseDto[] responseDtoArray = objectMapper.readValue(flaskResponse, MenuRecommendationResponseDto[].class);

        // 배열을 리스트로 변환
        return Arrays.asList(responseDtoArray);
    }
}
