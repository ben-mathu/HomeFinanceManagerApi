package com.benardmathu.hfms.data.tablerelationships.schedulejarrel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * Normalize schedule to encourage redundancies
 * @author bernard
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DbEnvironment.MONEY_JAR_SCHEDULE_REL_TB)
public class JarScheduleDateRel {
    @Id
    private Long id;
    
    @Column(name = DbEnvironment.HOUSEHOLD_ID)
    private Long householdId;
    
    @Column(name = DbEnvironment.MONEY_JAR_ID)
    private Long jarId;
    
    @Column(name = DbEnvironment.JAR_SCHEDULE_DATE, length = 45)
    private String scheduleDate = "";
    
    @Column(name = DbEnvironment.JAR_STATUS)
    private boolean jarStatus = false;
    
    @Column(name = DbEnvironment.PAYMENT_STATUS)
    private boolean paymentStatus = false;
    
    @Column(name = DbEnvironment.AMOUNT)
    private double amount;
}
