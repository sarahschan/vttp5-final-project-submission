package sg.edu.nus.iss.springboot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.springboot.models.PartnerStore;
import sg.edu.nus.iss.springboot.services.PartnerStoreService;

@RestController
@RequestMapping("/api/public/partners")
public class PublicPartnerStoreController {
    
    @Autowired
    PartnerStoreService partnerStoreService;

    @GetMapping()
    public ResponseEntity<?> getPartnerStores() {

        System.out.println("Request recieved to get partner stores");

        try {
            
            List<PartnerStore> partnerStores = partnerStoreService.getPartnerStores();

            System.out.println("Got partner stores");

            return ResponseEntity.ok().body(partnerStores);

        } catch (Exception e) {

            System.out.println("Error retrieving partner stores: " + e.getMessage());
            e.printStackTrace();
            
            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).body(error.toString());

        }
    }
}
