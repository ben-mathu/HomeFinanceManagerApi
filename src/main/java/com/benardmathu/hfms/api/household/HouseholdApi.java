package com.benardmathu.hfms.api.household;

import com.benardmathu.hfms.api.base.BaseServlet;
import com.benardmathu.hfms.data.household.HouseholdDao;
import com.benardmathu.hfms.data.household.model.Household;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdDao;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdRel;
import com.benardmathu.hfms.data.user.Members;
import com.benardmathu.hfms.data.user.UserDao;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.utils.DbEnvironment;
import static com.benardmathu.hfms.data.utils.DbEnvironment.USER_ID;
import static com.benardmathu.hfms.data.utils.URL.API;
import static com.benardmathu.hfms.data.utils.URL.HOUSEHOLDS_URL;
import com.benardmathu.hfms.utils.BufferRequestReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bernard
 */
@WebServlet(name = "HouseholdApi", urlPatterns = {API + HOUSEHOLDS_URL})
public class HouseholdApi extends BaseServlet {
    
    private UserHouseholdDao userHouseholdDao;
    private UserDao userDao;
    private HouseholdDao householdDao;
    
    public HouseholdApi() {
        userHouseholdDao = new UserHouseholdDao();
        userDao = new UserDao();
        householdDao = new HouseholdDao();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestStr = BufferRequestReader.bufferRequest(req);
        
        String uri = req.getRequestURI();
        UserHouseholdRel userHouseholdRel = gson.fromJson(requestStr, UserHouseholdRel.class);
        
        Report report = new Report();
        if (userHouseholdDao.update(userHouseholdRel) > 0) {
            report.setMessage("Successfully changed, your account will now be deleted.");
            report.setStatus(HttpServletResponse.SC_OK);
            
            resp.setStatus(HttpServletResponse.SC_OK);
            writer = resp.getWriter();
            writer.write(gson.toJson(report));
        } else {
            report.setMessage("Could not change ownership");
            report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            
            resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            writer = resp.getWriter();
            writer.write(gson.toJson(report));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter(USER_ID);
        
        UserHouseholdRel userHousehold = userHouseholdDao.get(userId);
        
        Members members = new Members();
        if (userHousehold.isOwner()) {
            
            List<UserHouseholdRel> userHouseholdList = userHouseholdDao.getAll(userHousehold.getHouseId());
            
            List<User> userList = new ArrayList<>();
            for (UserHouseholdRel userHouseholdRel : userHouseholdList) {
                User user = userDao.get(userHouseholdRel.getUserId());
                userList.add(user);
            }
            
            if (userList.size() <= 1) {
                resp.setStatus(HttpServletResponse.SC_OK);
                
                members.setHouseholdId(userHousehold.getHouseId());
            
                Report report = new Report();
                report.setMessage("There are no other members in your household.");
                report.setStatus(HttpServletResponse.SC_NO_CONTENT);

                members.setReport(report);

                String responseStr = gson.toJson(members);
                writer = resp.getWriter();
                writer.write(responseStr);
            } else {
                
                resp.setStatus(HttpServletResponse.SC_OK);
                
                members.setUsers(userList);
                members.setHouseholdId(userHousehold.getHouseId());

                Household household = householdDao.get(userHousehold.getHouseId());
                members.setHouseholdName(household.getName());

                Report report = new Report();
                report.setStatus(HttpServletResponse.SC_OK);
                report.setMessage("This is the owner of this household");
                members.setReport(report);

                writer = resp.getWriter();
                writer.write(gson.toJson(members));
            }
        } else {            
            resp.setStatus(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION);
            
            Report report = new Report();
            report.setMessage("This user is a member");
            report.setStatus(resp.getStatus());
            
            members.setReport(report);
            
            writer = resp.getWriter();
            writer.write(gson.toJson(members));
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestStr = BufferRequestReader.bufferRequest(req);
        
        String uri = req.getRequestURI();
        String householdId = req.getParameter(DbEnvironment.HOUSEHOLD_ID);
        Household household = new Household();
        household.setId(householdId);
        
        Report report = new Report();
        if (householdDao.delete(household) > 0) {
            report.setMessage("Successfully deleted, your account will now be deleted.");
            report.setStatus(HttpServletResponse.SC_OK);
            
            resp.setStatus(HttpServletResponse.SC_OK);
            writer = resp.getWriter();
            writer.write(gson.toJson(report));
        } else {
            report.setMessage("Could not delete household");
            report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            
            resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            writer = resp.getWriter();
            writer.write(gson.toJson(report));
        }
    }
}
