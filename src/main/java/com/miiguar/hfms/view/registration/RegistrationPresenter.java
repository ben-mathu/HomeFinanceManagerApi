package com.miiguar.hfms.view.registration;

import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.view.remote.users.UserApiService;
import com.miiguar.hfms.data.models.user.UserRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author bernard
 */
public class RegistrationPresenter implements RegistrationContract.Presenter {
    private RegistrationContract.View view;

    public RegistrationPresenter(RegistrationContract.View view) {
        this.view = view;
    }

    @Override
    public void saveUser(User user, HttpServletRequest req, HttpServletResponse resp) {
        UserRequest request = new UserRequest(user);

//        Call<Report> call = RetrofitClient.getRetrofitClient()
//                .create(UserApiService.class)
//                .saveUser(request);
//
//        call.enqueue(new Callback<>() {
//            @Override
//            public void onResponse(Call<Report> call, Response<Report> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    try {
//                        if (response.body().getStatus() != 200) {
//                            view.showError(response.body(), req, resp);
//                        } else {
//                            view.serveConfirmAccount(response.body(), req, resp);
//                        }
//                    } catch (IOException | ServletException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    try {
//                        Report report = new Report();
//                        report.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
//                        report.setMessage("An Error occurred while registering.");
//
//                        view.showError(report, req, resp);
//                    } catch (ServletException | IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Report> call, Throwable t) {
//                try {
//                    Report report = new Report();
//                    report.setMessage(
//                            "An error has occurred, " +
//                            "please contact the developer " +
//                            "here <a href=\"mailto:mathubenard@gmail.com\">mathubenard@gmail.com</a>"
//                    );
//                    view.showError(report, req, resp);
//                } catch (ServletException | IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
