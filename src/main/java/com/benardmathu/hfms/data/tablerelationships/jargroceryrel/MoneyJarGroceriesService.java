package com.benardmathu.hfms.data.tablerelationships.jargroceryrel;

import com.benardmathu.hfms.data.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Data access objects for MoneyJarGroceriesRel
 * @author bernard
 */
public class MoneyJarGroceriesService implements BaseService<MoneyJarGroceriesRel> {

    @Autowired
    private MoneyJarGroceriesRepository repository;

    @Override
    public MoneyJarGroceriesRel save(MoneyJarGroceriesRel moneyJarListRel) {
        return repository.save(moneyJarListRel);
    }

    @Override
    public MoneyJarGroceriesRel update(MoneyJarGroceriesRel item) {
        return null;
    }

    @Override
    public void delete(MoneyJarGroceriesRel item) {

    }

    @Override
    public MoneyJarGroceriesRel get(Long id) {
        return null;
    }

    @Override
    public List<MoneyJarGroceriesRel> getAll() {
        return null;
    }

    @Override
    public List<MoneyJarGroceriesRel> saveAll(ArrayList<MoneyJarGroceriesRel> items) {
        return null;
    }

    public List<String> getIdByJarId(Long jarId) {
        return repository.findByJarId(jarId);
    }
}
