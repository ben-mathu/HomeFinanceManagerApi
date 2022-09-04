package com.benardmathu.hfms.data.tablerelationships.schedulejarrel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JarScheduleDateRepository extends JpaRepository<JarScheduleDateRel, Long> {
    List<JarScheduleDateRel> findByHouseId(Long houseId);

    JarScheduleDateRel findByMoneyJarId(Long moneyJarId);

    List<JarScheduleDateRel> findAllByMoneyJarId(Long moneyJarId);
}
