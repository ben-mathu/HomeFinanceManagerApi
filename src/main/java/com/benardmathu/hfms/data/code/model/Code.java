package com.benardmathu.hfms.data.code.model;

import com.google.gson.annotations.SerializedName;
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
 * @author bernard
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = CODE_TB_NAME)
public class Code {
    @Id
    private Long id;

    @SerializedName(CODE)
    @Column(name = CODE)
    private String code = "";

    @SerializedName(USER_ID)
    @Column(name = USER_ID, unique = true)
    private String userId = "";

    @SerializedName(EMAIL_CONFIRMED)
    @Column(name = EMAIL_CONFIRMED)
    private boolean isEmailConfirmed = false;
}
