package com.bugdb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bugdb.Util.IteratorUtils;
import com.bugdb.domain.Bug;
import com.bugdb.domain.EsBug;
import com.bugdb.domain.EsUpdates;
import com.bugdb.domain.Updates;
import com.bugdb.service.IBugService;
import com.bugdb.service.IUpdatesService;
import com.bugdb.service.UpdatesService;
import com.bugdb.model.EsBugVO;
import com.google.gson.Gson;

@Controller
@RestController
@RequestMapping(value = "/esbug", produces = "application/json;charset=UTF-8")
public class ElasticBugContorller {

    @Autowired
    private IBugService iBugService;

    @Autowired
    private IUpdatesService iUpdatesService;
    @Autowired
    private UpdatesService updatesService;


    @RequestMapping(value = "synchronizeDB", method = RequestMethod.GET)
    public String bug() {
        deleteAll();
        Iterable<Bug> bugList = iBugService.findAll();
        int countBug = 0;
        int countUpdates = 0;
        for (Bug bug : bugList) {
            EsBug esBug = new EsBug(bug);
            EsBug eb = iBugService.save(esBug);
            if (eb != null)
                countBug++;

        }
        Iterable<Updates> updatesList = updatesService.findAll();
        for(Updates update : updatesList){
            EsUpdates esUpdates = new EsUpdates(update);
            EsUpdates esUpdate = iUpdatesService.save(esUpdates);
            if(esUpdate != null){
                countUpdates ++;
            }
        }
        Map map = new HashMap();
        map.put("Bug",countBug);
        map.put("Updates",countUpdates);
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    @RequestMapping(value = "/{queryString}", method = RequestMethod.GET)
    public List searchBug(@PathVariable("queryString") String queryString) {
        Iterable<EsBug> bugList = iBugService.search(queryString);
        Iterable<EsUpdates> updatesList = iUpdatesService.search(queryString);
        List<EsBug> esbugs = IteratorUtils.toList(bugList, EsBug.class);

        ArrayList<Integer> bugNos = new ArrayList<>();

        for (EsBug esbug : esbugs) {
            bugNos.add(esbug.getBugNo());
        }
        for (EsUpdates update :
                updatesList) {
            Integer bugNo = update.getBugId();
            if (!bugNos.contains(bugNo)){
                bugNos.add(bugNo);
                esbugs.add(iBugService.findByBugNo(bugNo));
            }
        }
        List<EsBugVO> bugs = IteratorUtils.toList(esbugs, EsBugVO.class);

        return bugs;
    }

    @RequestMapping(value = "/allBug", method = RequestMethod.GET)
    public List allBug() {
        Iterable<EsBug> bugList = iBugService.findAllEsBug();
        List<EsBugVO> esbugs = IteratorUtils.toList(bugList, EsBugVO.class);
        return esbugs;
    }

    @RequestMapping(value = "/deleteAll")
    public List deleteAll() {
        Iterable<EsBug> bugList = iBugService.deleteAll();
        List<EsBug> esbugs = IteratorUtils.toList(bugList);
        iUpdatesService.deleteAll();
        return esbugs;
    }
}
