package com.benardmathu.hfms.data.tablerelationships.userhouse;

import com.benardmathu.hfms.data.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author bernard
 */
@Service
public class UserHouseholdService implements BaseService<UserHouseholdRel> {
    public static final String TAG = UserHouseholdService.class.getSimpleName();

    @Autowired
    private UserHouseholdRepository repository;

    @Override
    public UserHouseholdRel save(UserHouseholdRel item) {
        return repository.save(item);
    }

    @Override
    public UserHouseholdRel update(UserHouseholdRel item) {
        return repository.save(item);
    }

    @Override
    public void delete(UserHouseholdRel item) {
        repository.delete(item);
    }

    @Override
    public UserHouseholdRel get(Long id) {
        Optional<UserHouseholdRel> optional = repository.findByUserId(id);
        UserHouseholdRel userHouseholdRel = optional.orElse(null);
        return userHouseholdRel;
    }

    @Override
    public List<UserHouseholdRel> getAll() {
        return repository.findAll();
    }
    
    public List<UserHouseholdRel> getAllByUserId(Long id) {
        return repository.findAllByUserId(id);
    }

    @Override
    public List<UserHouseholdRel> saveAll(ArrayList<UserHouseholdRel> items) {
        return repository.saveAll(items);
    }
}
