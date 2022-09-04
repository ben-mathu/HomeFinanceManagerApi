package com.benardmathu.hfms.data.household;

import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.household.model.Household;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Service
public class HouseholdService implements BaseService<Household> {
    public static final String TAG = HouseholdService.class.getSimpleName();

    @Autowired
    private HouseholdRepository repository;

    public Household getHousehold(Long householdId) {
        return repository.findById(householdId).orElse(null);
    }

    @Override
    public Household save(Household item) {
        return repository.save(item);
    }

    @Override
    public Household update(Household item) {
        return repository.save(item);
    }

    @Override
    public void delete(Household item) {
        repository.delete(item);
    }

    @Override
    public Household get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Household> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Household> saveAll(ArrayList<Household> items) {
        return repository.saveAll(items);
    }

    public List<User> getUserId(Long houseId) {
        return repository.findUserListById(houseId);
    }

    public Household getHouseholdByUserId(Long userId) {
        return repository.findByUserId(userId);
    }
}
