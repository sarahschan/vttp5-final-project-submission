package sg.edu.nus.iss.springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import sg.edu.nus.iss.springboot.models.NutritionRec;
import sg.edu.nus.iss.springboot.utilities.JsonParser;

@Service
public class NutritionRecService {
    
    @Value("${api.endpoint}")
    private String apiEndpoint;

    @Autowired
    JsonParser jsonParser;

    RestTemplate restTemplate = new RestTemplate();

    public NutritionRec getNutrition(int height, int weight, int age, String sex, String activityLevel, String goal) {
        
        String params = String.format("?height=%d&weight=%d&age=%d&sex=%s&activityLevel=%s&goal=%s", height, weight, age, sex, activityLevel, goal);
        String finalUrl = apiEndpoint + params;

        String response = restTemplate.getForObject(finalUrl, String.class);

        NutritionRec nutritionRec = jsonParser.toNutritionRec(response, height, weight, age, sex, activityLevel, goal);

        return nutritionRec;
        
    }
}
