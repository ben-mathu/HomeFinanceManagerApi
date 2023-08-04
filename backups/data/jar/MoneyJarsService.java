package com.benardmathu.hfms.data.jar;

import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.jar.model.MoneyJar;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bernard
 */
public class MoneyJarsService implements BaseService<MoneyJar> {
    public static final String TAG = MoneyJarsService.class.getSimpleName();

    @Autowired
    private MoneyJarRepository repository;

    @Override
    public MoneyJar save(MoneyJar item) {
        return repository.save(item);
    }

    @Override
    public MoneyJar update(MoneyJar item) {
        return repository.save(item);
    }

    @Override
    public void delete(MoneyJar item) {
        repository.delete(item);
    }

    @Override
    public MoneyJar get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<MoneyJar> getAll() {
        return repository.findAll();
    }

    @Override
    public List<MoneyJar> saveAll(ArrayList<MoneyJar> items) {
        return repository.saveAll(items);
    }
}
