package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.ErrorStatus;
import BabAl.BabalServer.apiPayload.exception.GeneralException;
import BabAl.BabalServer.domain.User;
import BabAl.BabalServer.dto.flaskRequest.IngredientAlpha;
import BabAl.BabalServer.dto.flaskRequest.RecipeRecommendation;
import BabAl.BabalServer.dto.flaskResponse.RecipeRecommendationResponse;
import BabAl.BabalServer.dto.request.RecipeRecommendationsDto;
import BabAl.BabalServer.dto.response.MenuRecommendationResponseDto;
import BabAl.BabalServer.dto.response.RecipeIngredientsResponseDto;
import BabAl.BabalServer.dto.response.RecipeRecommendationsResponseDto;
import BabAl.BabalServer.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeServiceImpl implements  RecipeService {
    private final ObjectMapper objectMapper; // 데이터를 JSON 객체로 변환
    private final UserRepository userRepository;

    @Value("${flask.url.search}")
    private String flaskUrlIngredientSearch;

    @Value("${flask.url.recommendation}")
    private String flaskUrlRecipeRecommendation;

    // 레시피 검색
    @Override
    public RecipeIngredientsResponseDto getIngredients(String alpha) throws JsonProcessingException {
        // 1. flask 로 알파벳 보내기
        IngredientAlpha alphabet = IngredientAlpha.ingredientAlpha(alpha);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(alphabet), httpHeaders);

        // 2. flask 응답 받기
        String flaskResponse = restTemplate.postForObject(flaskUrlIngredientSearch, entity, String.class);

        // 3. 응답 JSON을 RecipeIngredientsResponseDto로 변환하여 반환
        return objectMapper.readValue(flaskResponse, RecipeIngredientsResponseDto.class);
    }

    // 레시피 추천
    @Override
    public List<RecipeRecommendationsResponseDto> getRecommendations(String userEmail, RecipeRecommendationsDto ingredients) throws JsonProcessingException {
        // 0. user 정보 불러오기
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        // 1. flask 로 보낼 dto 세팅
        RecipeRecommendation dto = RecipeRecommendation.recipeRecommendation(user, ingredients.getIngredients());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(dto), httpHeaders);

        // 2. flask 응답 받기
        String flaskResponse = restTemplate.postForObject(flaskUrlRecipeRecommendation, entity, String.class);
        System.out.println("Flask Response: " + flaskResponse);  // 응답 로그 출력

        // 3. 응답 JSON을 RecipeRecommendationsResponseDto 변환
        // RecipeRecommendationResponse responseDto = objectMapper.readValue(flaskResponse, RecipeRecommendationResponse.class);

        List<RecipeRecommendationResponse> responseDtos = objectMapper.readValue(
                flaskResponse, new TypeReference<List<RecipeRecommendationResponse>>() {}
        );

        // 여러 개의 응답 DTO를 담을 리스트 생성
        List<RecipeRecommendationsResponseDto> answerDTOList = new ArrayList<>();

        for (RecipeRecommendationResponse responseDto : responseDtos) {

            // 4. steps 문자열에서 불필요한 문자 제거 후 리스트로 변환
            String stepsString = responseDto.getSteps();  // steps는 하나의 문자열로 전달됨

            // 4-1. 양 끝의 '['와 ']' 제거하고, 공백을 기준으로 나누기
            stepsString = stepsString.replace("[", "").replace("]", "");

            // 4-2. 각 항목을 ' '로 나누고, 공백을 기준으로 결합된 단어들을 리스트로 변환
            String[] stepsArray = stepsString.split("'\\s*,\\s*'"); // 각 단계마다 ' 로 구분되어 있으므로 이를 기준으로 나눈다.

            // 4-3. 각 단계를 리스트로 변환하고 불필요한 공백 및 따옴표 제거
            List<String> stepsList = new ArrayList<>();
            for (String step : stepsArray) {
                stepsList.add(step.replace("'", "").trim());  // ' 제거하고 공백 제거
            }

            List<String> result = new ArrayList<>();

            for (String step : stepsArray) {
                StringBuilder sb = new StringBuilder();
                boolean chk = false;
                String last = "empty";

                for (int i = 0; i < step.length(); i++) {
                    char c = step.charAt(i);

                    if (Character.isAlphabetic(c)) { // 알파벳은 추가
                        sb.append(c);
                        last = "alpha";
                        chk = false;
                    } else if (c == ' ' && last.equals("alpha")) {
                        // 공백 등장, 앞 글자가 알파벳이었으면 공백 삭제
                        last = "empty";
                    } else if (chk) {
                        continue;
                    } else if (c == ' ' && last.equals("empty")) {
                        // 공백 등장, 앞 글자가 공백
                        sb.append(' ');
                        chk = true;
                    }
                }

                // 결과 리스트에 추가
                result.add(sb.toString());
            }

            // 앞뒤 공백을 제거한 새로운 리스트 생성
            List<String> trimmedSteps = new ArrayList<>();
            for (String step : result) {
                trimmedSteps.add(step.trim());  // trim()을 사용하여 공백을 제거
            }

            // 5. steps를 리스트로 변환한 후 응답에 설정
            RecipeRecommendationsResponseDto answerDTO = RecipeRecommendationsResponseDto.recipeRecommendationsResponse(responseDto, trimmedSteps);
            answerDTOList.add(answerDTO);
        }

        // 6. 반환
        return answerDTOList;
    }
}
