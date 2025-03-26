package sg.edu.nus.iss.springboot.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.springboot.models.PartnerStore;

@Repository
public class PartnerStoreRepository {
 
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<PartnerStore> getPartnerStores() {
        return jdbcTemplate.query(Queries.SQL_GET_PARTNER_STORES, BeanPropertyRowMapper.newInstance(PartnerStore.class));
    }

}
