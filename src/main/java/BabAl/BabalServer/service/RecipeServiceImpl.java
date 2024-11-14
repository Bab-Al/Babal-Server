package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.ErrorStatus;
import BabAl.BabalServer.apiPayload.exception.GeneralException;
import BabAl.BabalServer.domain.User;
import BabAl.BabalServer.dto.flaskRequest.RecipeRecommendation;
import BabAl.BabalServer.dto.flaskResponse.RecipeRecommendationResponse;
import BabAl.BabalServer.dto.request.RecipeRecommendationsDto;
import BabAl.BabalServer.dto.response.RecipeIngredientsResponseDto;
import BabAl.BabalServer.dto.response.RecipeRecommendationsResponseDto;
import BabAl.BabalServer.dto.response.RecipeTagsResponseDto;
import BabAl.BabalServer.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.python.modules._hashlib;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeServiceImpl implements  RecipeService {
    private final ObjectMapper objectMapper; // 데이터를 JSON 객체로 변환
    private final UserRepository userRepository;

    @Value("${flask.url.recommendation}")
    private String flaskUrlRecipeRecommendation;

    @Value("${s3.recipe}")
    private String recipeUrl;

    // 레시피 재료 검색
    @Override
    public RecipeIngredientsResponseDto getIngredients(String alpha) {
        String alphabet = alpha.toLowerCase();

        try {
            checkFile(recipeUrl, "RAW_recipes.csv");

            // CSV 파일 읽기
            Set<String> allIngredients = new HashSet<>();
            try (CSVReader csvReader = new CSVReader(new FileReader("RAW_recipes.csv"))) {
                String[] record;
                while ((record = csvReader.readNext()) != null) {
                    String ingredientList = record[10];
                    List<String> ingredients = Arrays.asList(ingredientList.replaceAll("[\\[\\]']", "").split(", "));

                    // alpha로 시작하는 재료만 추가
                    for (String ingredient : ingredients) {
                        if (ingredient.toLowerCase().startsWith(alphabet)) {
                            allIngredients.add(ingredient);
                        }
                    }
                }
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }

            // 필터링된 재료를 정렬
            List<String> filteredIngredients = new ArrayList<>(allIngredients);
            Collections.sort(filteredIngredients);

            // DTO에 결과를 담아 반환
            RecipeIngredientsResponseDto responseDto = new RecipeIngredientsResponseDto(filteredIngredients.size(), filteredIngredients);
            return responseDto;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 레시피 태그 검색
    @Override
    public RecipeTagsResponseDto getTags(String alpha) throws JsonProcessingException {
        String alphabet = alpha.toLowerCase();

        try {
            checkFile(recipeUrl, "RAW_recipes.csv");

            // CSV 파일 읽기
            Set<String> allTags = new HashSet<>();
            try (CSVReader csvReader = new CSVReader(new FileReader("RAW_recipes.csv"))) {
                String[] record;
                while ((record = csvReader.readNext()) != null) {
                    String tagList = record[5];
                    List<String> tags = Arrays.asList(tagList.replaceAll("[\\[\\]']", "").split(", "));

                    // alpha로 시작하는 재료만 추가
                    for (String tag : tags) {
                        if (tag.toLowerCase().startsWith(alphabet)) {
                            allTags.add(tag);
                        }
                    }
                }
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }

            // 필터링된 재료를 정렬
            List<String> filteredTags = new ArrayList<>(allTags);
            Collections.sort(filteredTags);

            // DTO에 결과를 담아 반환
            RecipeTagsResponseDto responseDto = new RecipeTagsResponseDto(filteredTags.size(), filteredTags);
            return responseDto;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            RecipeRecommendationsResponseDto answerDTO = RecipeRecommendationsResponseDto.recipeRecommendationsResponse(responseDto);
            answerDTOList.add(answerDTO);
        }

        // 6. 반환
        return answerDTOList;
    }

    // 파일이 로컬에 존재하지 않으면 URL에서 다운로드
    private void checkFile(String url, String localFilename) throws IOException {
        Path path = Paths.get(localFilename);
        if (!Files.exists(path)) {
            System.out.println(localFilename + " 파일이 존재하지 않습니다. S3에서 다운로드 중...");
            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println(localFilename + " 파일 다운로드 및 저장 완료!");
        } else {
            System.out.println("로컬에 " + localFilename + " 파일이 이미 존재합니다. 저장된 파일을 불러옵니다.");
        }
    }
}
