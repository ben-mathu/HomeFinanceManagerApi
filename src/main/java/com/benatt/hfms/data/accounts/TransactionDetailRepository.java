package com.benatt.hfms.data.accounts;

import com.benatt.hfms.data.accounts.models.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Long> {
}
