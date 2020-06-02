package com.miiguar.hfms.view.registration;

import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.data.status.Report;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author bernard
 */
public interface RegistrationContract {
    interface Presenter {

        void saveUser(User user, HttpServletRequest request, HttpServletResponse response);
    }

    interface View {

        void serveConfirmAccount(Report body, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException;

        void showError(Report body, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
    }
}
