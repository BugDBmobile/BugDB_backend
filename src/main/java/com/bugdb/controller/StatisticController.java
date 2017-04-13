package com.bugdb.controller;

import com.bugdb.domain.Bug;
import com.bugdb.domain.User;
import com.bugdb.service.BugServiceDB;
import com.bugdb.service.UserService;
import com.bugdb.service.UtilService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jingwei on 13/04/2017.
 */
@Controller
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    private UtilService utilservice;
    @Autowired
    private BugServiceDB bugservice;
    @Autowired
    private UserService userService;

    @RequestMapping(value ="",params = {"userId","startTime","isClose"}, method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public @ResponseBody
    String statistic(@RequestParam int userId,@RequestParam String startTime,@RequestParam int isClose){
//        Timestamp timestamp = Timestamp.valueOf(startTime);
        LocalDateTime stl=LocalDateTime.parse(startTime);
        Timestamp st=Timestamp.valueOf(stl);
        Gson gson=new Gson();
        Map<Integer,Integer> result = new HashMap<>();

        result = countBugByFileBy(result, userId,isClose,st);

        return gson.toJson(result);
    }
    public Map countBugByFileBy(Map<Integer,Integer> stas, int userId, int isClose, Timestamp startTime){
        User user = userService.getUserById(userId);
        Gson gson=new Gson();
        if(!"m".equals(user.getRole())){
            int count = bugservice.countByStartTime(userId,isClose,startTime);
            if(stas.containsKey(userId)){
                int i = stas.get(userId);
                stas.remove(userId);
                stas.put(userId,(i + count));
            }else{
                stas.put(userId, count);
            }
            return stas;
        }
        else {
            List<User> users = userService.findByManager(userId);
            for (User us :
                    users) {
                Map<Integer,Integer> map = countBugByFileBy(stas,us.getId(),isClose,startTime);
            }
            int count = bugservice.countByStartTime(userId,isClose,startTime);
            if(stas.containsKey(userId)){
                int i = stas.get(userId);
                stas.remove(userId);
                stas.put(userId,(i + count));
            }else{
                stas.put(userId, count);
            }
        }

        return stas;
    }
}
