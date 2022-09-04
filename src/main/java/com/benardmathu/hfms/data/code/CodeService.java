package com.benardmathu.hfms.data.code;

import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.code.model.Code;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.utils.sender.EmailSession;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author bernard
 */
public class CodeService implements BaseService<Code> {
    public static final String TAG = CodeService.class.getSimpleName();

    @Autowired
    private CodeRepository codeRepository;

    public Code saveCode(Code item, String userId) {
        return codeRepository.save(item);
    }

    @Override
    public Code save(Code item) {
        return null;
    }

    @Override
    public Code update(Code item) {
        return codeRepository.save(item);
    }

    @Override
    public void delete(Code item) {
        codeRepository.delete(item);
    }

    @Override
    public Code get(Long id) {
        Optional<Code> codeOptional = codeRepository.getCodeByUserId(id);
        return codeOptional.orElse(null);
    }

    @Override
    public List<Code> getAll() {
        return null;
    }

    @Override
    public List<Code> saveAll(ArrayList<Code> items) {
        return codeRepository.saveAll(items);
    }

    public void emailConfirmed(String code) {
        Optional<Code> codeOptional = codeRepository.getCodeByCode(code);
        Code codeObj;
        if (codeOptional.isPresent()) {
            codeObj = codeOptional.get();
            codeObj.setEmailConfirmed(true);
            codeRepository.save(codeObj);
        }
    }

    public void sendCodeToEmail(User user, String code) {
        EmailSession session = new EmailSession();
        try {
            session.sendEmail(
                    user.getEmail(),
                    "Email Confirmation Code",
                    "<div style=\"text-align: center;\">" +
                            "<h5>User this code to confirm your email</h5>" +
                            "<h2>" + code + "</h2>" +
                            "</div>"
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
