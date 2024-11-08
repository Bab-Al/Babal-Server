package BabAl.BabalServer.service;

import BabAl.BabalServer.dto.response.RecipeIngredientsResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface RecipeService {

    // 레시피 검색
    RecipeIngredientsResponseDto getIngredients(String alpha) throws JsonProcessingException;
}
