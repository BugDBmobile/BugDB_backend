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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

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

    @RequestMapping(value ="",params = {"userId","startTime","endTime","isClose"}, method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public @ResponseBody
    String statistic(@RequestParam int userId,@RequestParam String startTime,@RequestParam String endTime,@RequestParam int isClose){
        Timestamp et;
        if(endTime == null)
            et=Timestamp.valueOf(LocalDateTime.now());
        else et =Timestamp.valueOf(endTime);
        Timestamp st=Timestamp.valueOf(startTime);
        Gson gson=new Gson();
        Map<Integer,Integer> result = new HashMap<>();
        result = countBugByUser(result, userId,isClose,st,et);
        return gson.toJson(result);
    }
    @RequestMapping(value ="{granularity}",params = {"userId","startTime","endTime","isClose"}, method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public @ResponseBody
    String statisticByGranularity(@PathVariable String granularity, @RequestParam int userId, @RequestParam String startTime, @RequestParam String endTime, @RequestParam int isClose) throws ParseException {
        Timestamp et;
        if(endTime == "" || endTime == null)
            et=Timestamp.valueOf(LocalDateTime.now());
        else {
            LocalDateTime stle=LocalDateTime.parse(endTime);
            et =Timestamp.valueOf(stle);
        }
        LocalDateTime stl=LocalDateTime.parse(startTime);
        Timestamp st=Timestamp.valueOf(stl);
        Gson gson=new Gson();
        Map<Timestamp,Map<Integer,Integer>> result = new HashMap<>();

        switch (granularity){
            case "day":
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                Timestamp ss = Timestamp.valueOf(sdf.format(st));
                while(ss.before(et)){
                    Calendar c=Calendar.getInstance();
                    c.setTime(ss);
                    c.add(Calendar.DAY_OF_MONTH,1);
                    Timestamp tt = Timestamp.valueOf(sdf.format(c.getTime()));
                    Map<Integer,Integer> res = new HashMap<>();
                    res = countBugByUser(res, userId,isClose,ss,tt);
                    result.put(ss,res);
                    ss = tt;
                }
            }
            break;
            case "week":
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                Timestamp ss = Timestamp.valueOf(sdf.format(st));
                while(ss.before(et)){
                    Calendar c=Calendar.getInstance();
                    c.setTime(ss);
                    c.add(Calendar.DAY_OF_MONTH,7);
                    Timestamp tt = Timestamp.valueOf(sdf.format(c.getTime()));
                    Map<Integer,Integer> res = new HashMap<>();
                    res = countBugByUser(res, userId,isClose,ss,tt);
                    result.put(ss,res);
                    ss = tt;
                }
            }

                break;

        }
        return gson.toJson(result);
    }


    public Map countBugByUser(Map<Integer,Integer> stas, int userId, int isClose, Timestamp startTime, Timestamp endTime){
        Map<Integer,Integer> countMap = new HashMap<>();
        Map<Integer,Integer> result = new HashMap<>();

        countMap = countBugByFileBy(countMap, userId,isClose,startTime ,endTime);

        int count = countMap.get(userId);
        List<User> users = userService.findByManager(userId);
        for (User user :
                users) {
            result.put(user.getId(),countMap.get(user.getId()));
            count -= countMap.get(user.getId());
        }
        result.put(userId,count);
        return result;
    }
    public Map countBugByFileBy(Map<Integer,Integer> stas, int userId, int isClose, Timestamp startTime, Timestamp endTime){
        User user = userService.getUserById(userId);
        if(!"m".equals(user.getRole())){
            int count = bugservice.countByStartTime(userId,isClose,startTime, endTime);
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
                Map<Integer,Integer> map = countBugByFileBy(stas,us.getId(),isClose,startTime, endTime);
                if(stas.containsKey(userId)){
                    int i = stas.get(userId);
                    stas.remove(userId);
                    stas.put(userId,(i + map.get(us.getId())));
                }else{
                    stas.put(userId, map.get(us.getId()));
                }
            }
            int count = bugservice.countByStartTime(userId,isClose,startTime, endTime);
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
