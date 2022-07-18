package com.benardmathu.hfms.data.code.model;

import com.benardmathu.hfms.data.user.model.User;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    @SerializedName("user")
    @OneToOne
    @JoinColumn(name = USER_ID, referencedColumnName = USER_ID)
    private User user;

    @SerializedName(EMAIL_CONFIRMED)
    @Column(name = EMAIL_CONFIRMED)
    private boolean isEmailConfirmed = false;
}
