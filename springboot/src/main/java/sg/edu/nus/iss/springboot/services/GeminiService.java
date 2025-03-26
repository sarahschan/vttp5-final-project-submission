package sg.edu.nus.iss.springboot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import sg.edu.nus.iss.springboot.utilities.JsonParser;

@Service
public class GeminiService {
    
    @Value("${gemini.api.key}")
    private String geminiKey;
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JsonParser jsonParser;

    RestTemplate restTemplate = new RestTemplate();


    public String generateRecipe(List<String> ingredients) throws JsonMappingException, JsonProcessingException {

        ObjectNode requestBody = objectMapper.createObjectNode();
        ArrayNode contents = requestBody.putArray("contents");
        ObjectNode content = contents.addObject();
        ArrayNode parts = content.putArray("parts");
        ObjectNode part = parts.addObject();
        part.put("text", createPrompt(ingredients));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = GEMINI_API_URL + "?key=" + geminiKey;

        RequestEntity<String> request = RequestEntity.post(url)
                                            .headers(headers)
                                            .body(requestBody.toString());

        String response = restTemplate.exchange(request, String.class).getBody();
            
        JsonNode responseJson = objectMapper.readTree(response);
        String recipeJsonText = responseJson
            .path("candidates")
            .path(0)
            .path("content")
            .path("parts")
            .path(0)
            .path("text")
            .asText();

        String recipe = jsonParser.cleanGeminiRecipe(recipeJsonText);

        return recipe;
    }


    private String createPrompt(List<String> ingredients) {
        return "Create a recipe using only these ingredients: " + 
               String.join(", ", ingredients) + 
               ". If included, you do not have to use cooking oil, olive oil, soy sauce, salt, pepper, onions, garlic, or rice. The rest of the ingredients must be used to their full amounts. Return ONLY a valid JSON object with the following structure (no markdown, no explanation):\n" +
               "{\n" +
               "  \"title\": \"Recipe Title\",\n" +
               "  \"description\": \"Short recipe description\",\n" +
               "  \"ingredients\": [\"ingredient 1 with quantity\", \"ingredient 2 with quantity\"],\n" +
               "  \"prepTime\": \"prep time in minutes\",\n" +
               "  \"cookTime\": \"cook time in minutes\",\n" +
               "  \"steps\": [\"Step 1 description\", \"Step 2 description\"]\n" +
               "}";
    }
}
