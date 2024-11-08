package BabAl.BabalServer.service;

import BabAl.BabalServer.dto.flaskRequest.IngredientAlpha;
import BabAl.BabalServer.dto.response.MenuRecommendationResponseDto;
import BabAl.BabalServer.dto.response.RecipeIngredientsResponseDto;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeServiceImpl implements  RecipeService {
    private final ObjectMapper objectMapper; // 데이터를 JSON 객체로 변환

    @Value("${flask.url.search}")
    private String flaskUrl;

    @Override
    public RecipeIngredientsResponseDto getIngredients(String alpha) throws JsonProcessingException {
        // 1. flask 로 알파벳 보내기
        IngredientAlpha alphabet = IngredientAlpha.ingredientAlpha(alpha);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(alphabet), httpHeaders);

        // 2. flask 응답 받기
        String flaskResponse = restTemplate.postForObject(flaskUrl, entity, String.class);

        // 3. 응답 JSON을 RecipeIngredientsResponseDto로 변환하여 반환
        return objectMapper.readValue(flaskResponse, RecipeIngredientsResponseDto.class);
    }
}
