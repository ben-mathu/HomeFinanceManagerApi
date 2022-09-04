package com.benardmathu.hfms.data.income;

import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.income.model.Income;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Member methods to create and update the income table
 * @author bernard
 */
public class IncomeService implements BaseService<Income> {
    public static final String TAG = IncomeService.class.getSimpleName();

    @Autowired
    private IncomeRepository incomeRepository;

    @Override
    public Income save(Income item) {
        return incomeRepository.save(item);
    }

    @Override
    public Income update(Income item) {
        return incomeRepository.save(item);
    }

    @Override
    public void delete(Income item) {
        incomeRepository.delete(item);
    }

    @Override
    public Income get(Long id) {
        Optional<Income> optional =  incomeRepository.findByUserId(id);
        return optional.orElse(null);
    }

    @Override
    public List<Income> getAll() {
        return incomeRepository.findAll();
    }

    @Override
    public List<Income> saveAll(ArrayList<Income> items) {
        return incomeRepository.saveAll(items);
    }
}
