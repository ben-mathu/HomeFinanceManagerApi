package com.benardmathu.hfms.api.household;

import com.benardmathu.hfms.api.base.BaseController;
import com.benardmathu.hfms.data.household.HouseholdBaseService;
import com.benardmathu.hfms.data.household.HouseholdRepository;
import com.benardmathu.hfms.data.household.model.Household;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdBaseService;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdRel;
import com.benardmathu.hfms.data.user.Members;
import com.benardmathu.hfms.data.user.UserBaseService;
import com.benardmathu.hfms.data.user.UserRepository;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.utils.DbEnvironment;
import static com.benardmathu.hfms.data.utils.DbEnvironment.USER_ID;
import static com.benardmathu.hfms.data.utils.URL.HOUSEHOLDS_URL;
import com.benardmathu.hfms.utils.BufferRequestReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bernard
 */
@RestController
@RequestMapping(name = "HouseholdApi", value = HOUSEHOLDS_URL)
public class HouseholdApi extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseholdRepository householdRepository;

    private UserHouseholdBaseService userHouseholdDao;
    private UserBaseService userDao;
    private HouseholdBaseService householdDao;
    
    public HouseholdApi() {
        userHouseholdDao = new UserHouseholdBaseService();
        userDao = new UserBaseService();
        householdDao = new HouseholdBaseService();
    }

    @PutMapping
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

    @GetMapping
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
    
    @DeleteMapping
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestStr = BufferRequestReader.bufferRequest(req);
        
        String uri = req.getRequestURI();
        Long householdId = Long.parseLong(req.getParameter(DbEnvironment.HOUSEHOLD_ID));
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
