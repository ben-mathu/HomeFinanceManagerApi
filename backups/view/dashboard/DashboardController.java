package com.benardmathu.hfms.view.dashboard;

import com.benardmathu.hfms.view.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.benardmathu.hfms.utils.Constants.REFRESH;

/**
 * @author bernard
 */
@Controller("/dashboard/controller/*")
public class DashboardController extends BaseController {
    private static final long serialVersionUID = 1L;

    @GetMapping
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String title = req.getParameter("title");

        if (!REFRESH.equals(title)) {
            String url = BaseController.getMap().get(title) == null ? "" : BaseController.getMap().get(title);
            if (!url.isEmpty()) {
                UrlResponse response = new UrlResponse();
                response.title = title;
                response.data = title;
                response.url = req.getContextPath() + url;

                String responseStr = gson.toJson(response);

                resp.setStatus(HttpServletResponse.SC_OK);
                writer = resp.getWriter();
                writer.write(responseStr);
            }
        } else {
            UrlResponse response = new UrlResponse();
            response.url = req.getContextPath();
            response.title = "";
            response.data = "";

            String responseStr = gson.toJson(response);

            resp.setStatus(HttpServletResponse.SC_OK);
            writer = resp.getWriter();
            writer.write(responseStr);
        }
    }

    private class UrlResponse {
        private String url = "";
        private String title = "";
        private String data = "";

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}