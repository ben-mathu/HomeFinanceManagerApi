package com.benardmathu.hfms.data.code;

import com.benardmathu.hfms.data.code.model.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> {
    Optional<Code> getCodeByUserId(Long id);

    Optional<Code> getCodeByCode(String code);
}
