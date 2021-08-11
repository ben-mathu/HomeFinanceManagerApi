package com.benardmathu.hfms.view.dashboard;

import com.benardmathu.hfms.data.household.HouseholdDto;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdRel;
import com.benardmathu.hfms.data.user.Members;
import com.benardmathu.hfms.data.user.UserDto;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.utils.DbEnvironment;
import static com.benardmathu.hfms.data.utils.DbEnvironment.USER_ID;
import static com.benardmathu.hfms.data.utils.URL.GET_HOUSEHOLD_MEMBERS;
import static com.benardmathu.hfms.data.utils.URL.UPDATE_USER_HOUSEHOLD_REL;
import static com.benardmathu.hfms.utils.Constants.TOKEN;
import com.benardmathu.hfms.utils.InitUrlConnection;
import com.benardmathu.hfms.view.base.BaseServlet;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manage HTTP request methods for CRUD operations on household table
 * @author bernard
 */
@WebServlet(name = "HouseholdServletController", urlPatterns = {"/dashboard/household-controller/*"})
public class HouseholdServletController extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        
        String userId = USER_ID + "=" + req.getParameter(USER_ID);
        
        String token = req.getParameter(TOKEN);
        
        String uri = req.getRequestURI();
        
        InitUrlConnection<User> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(GET_HOUSEHOLD_MEMBERS + "?" + userId, token, "GET");
        
        String line;
        StringBuilder builder = new StringBuilder();
        
        while((line = streamReader.readLine()) != null) {
            builder.append(line);
        }
        
        Members members = gson.fromJson(builder.toString(), Members.class);
        
        if (uri.endsWith("check-onwership") && members.getReport().getStatus() == 200) {
            StringBuilder notifier = new StringBuilder();
            notifier.append("<input id=\"householdIdForMessage\" type=\"text\" value=\"")
                    .append(members.getHouseholdId()).append("\" hidden/>")
                    .append("There are ")
                    .append(members.getUsers().size()).append(" members in ")
                    .append(members.getHouseholdName()).append("<br>")
                    .append("Make <select id=\"selectNewOwner\">");
            
            for (User user : members.getUsers()) {
                notifier.append("<option value=")
                        .append(user.getUserId())
                        .append(">")
                        .append(user.getUsername())
                        .append("</option>");
            }
            
            notifier.append("</select>")
                    .append(" Owner of household <input id=\"btnChangeOwner\" class=\"btn2\" type=\"button\" action=\"changeOwnership()\" value=\"Done\"/><br>")
                    .append("<br> <input id=\"btnDeleteHouseHld\" class=\"btn3-warn\" type=\"button\" value=\"Delete Anyway\"/>");
            
            resp.setStatus(members.getReport().getStatus());
            
            writer = resp.getWriter();
            writer.write(notifier.toString());
        } else if (members.getReport().getStatus() == 204) {
            resp.setStatus(HttpServletResponse.SC_OK);
            
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<input id=\"householdIdForMessage\" type=\"text\" value=\"")
                    .append(members.getHouseholdId()).append("\" hidden/>")
                    .append("You have no members in your household. Do you want to delete it?")
                    .append("<br> <input id=\"btnDeleteHouseHld\" class=\"btn3-warn\" type=\"button\" action=\"deleteHousehold();\" value=\"Delete\"/>")
                    .append("<br> <input id=\"btnCancelDeletion\" class=\"btn2\" type=\"button\" action=\"cancelAccountDeletion();\" value=\"Cancel\"/>");
            
            writer = resp.getWriter();
            writer.write(stringBuilder.toString());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
        
        String token = req.getParameter(TOKEN);
        String userId = req.getParameter(USER_ID);
        String householdId = req.getParameter(DbEnvironment.HOUSEHOLD_ID);
        String uri = req.getRequestURI();
        
        UserHouseholdRel userHouseholdRel = new UserHouseholdRel();
        userHouseholdRel.setHouseId(householdId);
        userHouseholdRel.setUserId(userId);
        userHouseholdRel.setOwner(true);
        
        InitUrlConnection<UserHouseholdRel> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(userHouseholdRel, UPDATE_USER_HOUSEHOLD_REL, token, "PUT");
//        if (uri.endsWith("change-onwership")) {
//            streamReader = conn.getReader(userHouseholdRel, UPDATE_USER_HOUSEHOLD_REL, token, "PUT");
//        }
        
        String line;
        StringBuilder builder = new StringBuilder();
        
        while((line = streamReader.readLine()) != null) {
            builder.append(line);
        }
        
        Report report = gson.fromJson(builder.toString(), Report.class);
        resp.setStatus(report.getStatus());
        
        writer = resp.getWriter();
        writer.write(builder.toString());
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
        
        String token = req.getParameter(TOKEN);
        String householdId = DbEnvironment.HOUSEHOLD_ID + "=" + req.getParameter(DbEnvironment.HOUSEHOLD_ID);
        String uri = req.getRequestURI();
        
        InitUrlConnection<UserHouseholdRel> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(UPDATE_USER_HOUSEHOLD_REL + "?" + householdId, token, "DELETE");
//        if (uri.endsWith("change-onwership")) {
//            streamReader = conn.getReader(userHouseholdRel, UPDATE_USER_HOUSEHOLD_REL, token, "PUT");
//        }
        
        String line;
        StringBuilder builder = new StringBuilder();
        
        while((line = streamReader.readLine()) != null) {
            builder.append(line);
        }
        
        Report report = gson.fromJson(builder.toString(), Report.class);
        resp.setStatus(report.getStatus());
        
        writer = resp.getWriter();
        writer.write(builder.toString());
    }
}
