package sg.edu.nus.iss.springboot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.models.PartnerStore;
import sg.edu.nus.iss.springboot.repositories.PartnerStoreRepository;

@Service
public class PartnerStoreService {
 
    @Autowired
    PartnerStoreRepository partnerStoreRepository;

    public List<PartnerStore> getPartnerStores() {
        return partnerStoreRepository.getPartnerStores();
    }
}
