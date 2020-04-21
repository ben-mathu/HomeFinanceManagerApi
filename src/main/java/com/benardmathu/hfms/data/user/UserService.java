package com.benardmathu.hfms.data.user;

import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.household.model.Household;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.utils.DbEnvironment;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Service
public class UserService implements BaseService<User> {
    public static final String TAG = UserService.class.getSimpleName();

    @Autowired
    private UserRepository repository;

    public Household getHouseholdByUserId(Long userId) {
        return repository.findHouseholdByUserId(userId);
    }

    public User getUserDetails(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public User update(User item) {
        return repository.save(item);
    }

    @Override
    public void delete(User item) {
        repository.delete(item);
    }

    @Override
    public User get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    public List<User> getAll(Long id) {
        return null;
    }

    @Override
    public List<User> saveAll(ArrayList<User> items) {
        return repository.saveAll(items);
    }

    public User validateCredentials(String username) {
        return repository.findByUsername(username);
    }

    public User updateEmail(User user) {
        return repository.save(user);
    }
}
