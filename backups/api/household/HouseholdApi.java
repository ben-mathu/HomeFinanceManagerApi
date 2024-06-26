package com.benardmathu.hfms.api.household;

import com.benardmathu.hfms.api.base.BaseController;
import com.benardmathu.hfms.data.household.HouseholdService;
import com.benardmathu.hfms.data.household.model.Household;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdService;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdRel;
import com.benardmathu.hfms.data.user.Members;
import com.benardmathu.hfms.data.user.UserService;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.utils.DbEnvironment;
import static com.benardmathu.hfms.data.utils.DbEnvironment.USER_ID;
import static com.benardmathu.hfms.data.utils.URL.HOUSEHOLDS_URL;
import static javax.servlet.http.HttpServletResponse.SC_NOT_MODIFIED;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private UserHouseholdService userHouseholdService;

    @Autowired
    private UserService userService;

    @Autowired
    private HouseholdService householdService;

    @PutMapping
    protected ResponseEntity<Report> changeHousehold(@RequestBody UserHouseholdRel userHouseholdRel,
                                             HttpServletRequest req, HttpServletResponse resp
    ) throws ServletException, IOException {
        Report report = new Report();
        UserHouseholdRel rel = userHouseholdService.update(userHouseholdRel);
        if (rel != null) {
            report.setMessage("Successfully changed, your account will now be deleted.");
            report.setStatus(HttpServletResponse.SC_OK);
            
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            report.setMessage("Could not change ownership");
            report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            
            resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        }
        return new ResponseEntity<>(report, HttpStatus.ACCEPTED);
    }

    @GetMapping
    protected ResponseEntity<Members> getHousehold(@RequestParam(USER_ID) String userId,
                                HttpServletRequest req, HttpServletResponse resp
    ) throws ServletException, IOException {

        UserHouseholdRel userHousehold = userHouseholdService.get(Long.parseLong(userId));
        Members members = new Members();

        if (userHousehold.isOwner()) {
            List<UserHouseholdRel> userHouseholdList = userHouseholdService.getAll();
            List<User> userList = new ArrayList<>();

            for (UserHouseholdRel userHouseholdRel : userHouseholdList) {
                User user = userService.get(userHouseholdRel.getUserId());
                userList.add(user);
            }
            
            if (userList.size() <= 1) {
                resp.setStatus(HttpServletResponse.SC_OK);
                
                members.setHouseholdId(userHousehold.getHouseId());
            
                Report report = new Report();
                report.setMessage("There are no other members in your household.");
                report.setStatus(HttpServletResponse.SC_NO_CONTENT);

                members.setReport(report);

                return new ResponseEntity<>(members, HttpStatus.OK);
            } else {
                
                resp.setStatus(HttpServletResponse.SC_OK);
                
                members.setUsers(userList);
                members.setHouseholdId(userHousehold.getHouseId());

                Household household = householdService.get(userHousehold.getHouseId());
                members.setHouseholdName(household.getName());

                Report report = new Report();
                report.setStatus(HttpServletResponse.SC_OK);
                report.setMessage("This is the owner of this household");
                members.setReport(report);

                return new ResponseEntity<>(members, HttpStatus.OK);
            }
        } else {            
            resp.setStatus(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION);
            
            Report report = new Report();
            report.setMessage("This user is a member");
            report.setStatus(resp.getStatus());
            
            members.setReport(report);
            
            return new ResponseEntity<>(members, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }
    
    @DeleteMapping
    protected ResponseEntity<Report> deleteHousehold(@RequestParam(DbEnvironment.HOUSEHOLD_ID) Long householdId,
                                   HttpServletRequest req, HttpServletResponse resp
    ) throws ServletException, IOException {

//        String requestStr = BufferRequestReader.bufferRequest(req);
//
//        String uri = req.getRequestURI();
//        Long householdId = Long.parseLong(req.getParameter(DbEnvironment.HOUSEHOLD_ID));
        Household household = new Household();
        household.setId(householdId);
        
        Report report = new Report();
        HttpStatus statusCode;
        householdService.delete(household);
        report.setMessage("Successfully deleted, your account will now be deleted.");
        report.setStatus(HttpServletResponse.SC_OK);

        resp.setStatus(HttpServletResponse.SC_OK);

        statusCode = HttpStatus.OK;
//        if ( > 0) {
//
//        } else {
//            report.setMessage("Could not delete household");
//            report.setStatus(SC_NOT_MODIFIED);
//
//            resp.setStatus(SC_NOT_MODIFIED);
//
//            statusCode = HttpStatus.NOT_MODIFIED;
//        }

        return new ResponseEntity<>(report, statusCode);
    }
}
