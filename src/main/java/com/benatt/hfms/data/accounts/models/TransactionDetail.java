package com.benatt.hfms.data.accounts.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class TransactionDetail {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String paidTo;
    private double painOut;
    private String paidFrom;
    private double paidIn;
    @JsonBackReference("account-tractions")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Account account;
}
