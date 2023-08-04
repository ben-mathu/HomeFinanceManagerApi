package com.benardmathu.hfms.data.tablerelationships.schedulejarrel;

import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.utils.DbEnvironment;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author bernard
 */
public class MoneyJarScheduleServices implements BaseService<JarScheduleDateRel> {

    @Autowired
    JarScheduleDateRepository repository;

    @Override
    public JarScheduleDateRel save(JarScheduleDateRel item) {
        return repository.save(item);
    }

    @Override
    public JarScheduleDateRel update(JarScheduleDateRel item) {
        return repository.save(item);
    }

    @Override
    public void delete(JarScheduleDateRel item) {

    }

    public List<JarScheduleDateRel> getAll(Long houseId) {
        return repository.findByHouseId(houseId);
    }

    public JarScheduleDateRel getWithStatusFalse(Long moneyJarId) {
        return repository.findByMoneyJarId(moneyJarId);
    }

    @Override
    public JarScheduleDateRel get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<JarScheduleDateRel> getAll() {
        return null;
    }

    @Override
    public List<JarScheduleDateRel> saveAll(ArrayList<JarScheduleDateRel> items) {
        return null;
    }

    public List<JarScheduleDateRel> getAllByJarId(Long moneyJarId) {
        return repository.findAllByMoneyJarId(moneyJarId);
    }

    public List<JarScheduleDateRel> getAllPaid(Long householdId, String from, String to) {
        return repository.findByHouseId(householdId);
    }
}
