package com.benardmathu.hfms.data.tablerelationships.jarexpenserel;

import com.benardmathu.hfms.data.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Data access class extends BaseDao
 * @author bernard
 */
public class MoneyJarExpenseService implements BaseService<MoneyJarExpenseRel> {

    @Autowired
    private MoneyJarExpenseRepository repository;

    @Override
    public MoneyJarExpenseRel save(MoneyJarExpenseRel item) {
        return repository.save(item);
    }

    @Override
    public MoneyJarExpenseRel update(MoneyJarExpenseRel item) {
        return null;
    }

    @Override
    public void delete(MoneyJarExpenseRel item) {

    }

    @Override
    public MoneyJarExpenseRel get(Long id) {
        return null;
    }

    @Override
    public List<MoneyJarExpenseRel> getAll() {
        return null;
    }

    @Override
    public List<MoneyJarExpenseRel> saveAll(ArrayList<MoneyJarExpenseRel> items) {
        return null;
    }

    public Long getIdByJarId(Long jarId) {
        return repository.findByJarId(jarId);
    }
}
