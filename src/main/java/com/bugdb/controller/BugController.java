package com.bugdb.controller;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bugdb.domain.*;
import com.bugdb.model.BugVO;
import com.bugdb.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
@Controller
@RequestMapping("/")
public class BugController {
	
	@Autowired
	private BugServiceDB bugservice;
	@Autowired
	private UpdatesService updateservice;
	@Autowired
	private IUpdatesService iUpdatesService;
	@Autowired
	private IBugService iBugService;
	@Autowired
	private UtilService utilservice;
	@Autowired
	private UserService userservice;
	@Autowired
	private ISearchHistoryService iSearchHistoryService;

	@RequestMapping(value ="findByBugNo",params = {"bugNo"}, method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody String findByBugNo(HttpServletRequest request,@RequestParam int bugNo){
		Bug bug=bugservice.findByBugNo(bugNo);
		BugVO bugVO = new BugVO(bug);
		bugVO.setAssignedName(userservice.findById(bug.getAssigned()).getUserName());
		bugVO.setStatusIdName(utilservice.findStatusById(bug.getStatusId()).getDescription());
		bugVO.setProductIdName(utilservice.findProductById(bug.getProductId()).getDescription());
		bugVO.setFiledByName(userservice.findById(bug.getFiledBy()).getUserName());
		Component component = utilservice.findComponentById(bug.getComponentId());
		bugVO.setComponentIdName(component.getName());
		bugVO.setComponentIdDes(component.getDescription());
		bugVO.setFixedByName(userservice.findById(bug.getFixedBy()).getUserName());
		bugVO.setOsIdName(utilservice.fingOsById(bug.getOsId()).getDescription());
		bugVO.setSeverityIdName(utilservice.findSeverityById(bug.getSeverityId()).getDescription());

		Gson gson=new Gson();
		return gson.toJson(bugVO);
	}
	

	@RequestMapping(value ="findByAssigned",params = {"userId"}, method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody String findByAssigned(HttpServletRequest request,@RequestParam int userId){
		List<Bug> result=bugservice.findByAssigned(userId);
		Gson gson=new Gson();
		return gson.toJson(result);
	}
	
	@RequestMapping(value ="findByFiledBy",params = {"userId"}, method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody String findByFiledBy(@RequestParam int userId){

		Gson gson=new Gson();
		List<Bug> result = new ArrayList<>();

		result = findBugByFileBy(result, userId);

		return gson.toJson(result);
	}
	public List<Bug> findBugByFileBy(List<Bug> bugs, int userId){
		User user = userservice.getUserById(userId);
		Gson gson=new Gson();
		if(!"m".equals(user.getRole())){
			bugs.addAll(bugservice.findByfiledBy(userId));
			return bugs;
		}
		else {
			List<User> users = userservice.findByManager(userId);
			for (User us :
					users) {
				List<Bug> bugSub = findBugByFileBy(bugs,us.getId());
			}
			bugs.addAll(bugservice.findByfiledBy(userId));
		}

		return bugs;
	}

	@RequestMapping(value ="findSearchHistory",params = {"userId"}, method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody String findSearchHistoryByUserId(@RequestParam int userId){
		List<SearchHistory> result = iSearchHistoryService.findBySearchUserId(userId);
		Gson gson=new Gson();
		return gson.toJson(result);
	}
	@RequestMapping(value ="findSearchHistoryById",params = {"id"}, method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody String findSearchHistoryById(@RequestParam int id){
		SearchHistory result = iSearchHistoryService.findById(id);
		Gson gson=new Gson();
		return gson.toJson(result);
	}
//	@RequestMapping(value ="createSearchHistory",params = {"productId","component","status","assigned","severity","tag","filedBy"}, method = RequestMethod.GET)
//	public @ResponseBody String createHistory(@RequestParam Integer productId,
//											@RequestParam Integer component,
//											@RequestParam Integer status,
//											@RequestParam Integer assigned,
//											@RequestParam Integer severity,
//											@RequestParam String tag,
//											@RequestParam Integer filedBy){
//		Gson gson=new Gson();
//		SearchHistory searchHistory = new SearchHistory();
//		searchHistory.setProductId(productId);
//		searchHistory.setComponentId(component);
//		searchHistory.setStatusId(status);
//		searchHistory.setAssigned(assigned);
//		searchHistory.setSeverityId(severity);
//		searchHistory.setTags(tag);
//		searchHistory.setFiledBy(filedBy);
//		searchHistory.setCreatTime(Timestamp.valueOf(LocalDateTime.now()));
//		SearchHistory result = iSearchHistoryService.save(searchHistory);
//		return gson.toJson(result);
//	}
	@RequestMapping(value ="saveSearchHistory",params = {"id","searchName","productId","component","status","assigned","severity","tag","filedBy","startTime","endTime","userId"}, method = RequestMethod.GET)
	public @ResponseBody String saveHistory(@RequestParam Integer id,
											@RequestParam String searchName,
											@RequestParam Integer productId,
											@RequestParam Integer component,
											@RequestParam Integer status,
											@RequestParam Integer assigned,
											@RequestParam Integer severity,
											@RequestParam String tag,
											@RequestParam Integer filedBy,
                                            @RequestParam String startTime,
                                            @RequestParam String endTime,
                                            @RequestParam Integer userId){
		Gson gson=new Gson();

		if(id == null){
			SearchHistory searchHistory = new SearchHistory();
			searchHistory.setSearchName(searchName);
			searchHistory.setProductId(productId);
			searchHistory.setComponentId(component);
			searchHistory.setStatusId(status);
			searchHistory.setAssigned(assigned);
			searchHistory.setSeverityId(severity);
			searchHistory.setTags(tag);
			searchHistory.setFiledBy(filedBy);
			searchHistory.setSearchUserId(userId);
			searchHistory.setCreatTime(Timestamp.valueOf(LocalDateTime.now()));
            if(startTime != null){
                searchHistory.setStartTime(Timestamp.valueOf(LocalDateTime.parse(startTime)));
            }
            if(endTime != null){
                searchHistory.setEndTime(Timestamp.valueOf(LocalDateTime.parse(endTime)));
            }
			SearchHistory result = iSearchHistoryService.save(searchHistory);
			return gson.toJson(result);
		}
		SearchHistory searchHistory = iSearchHistoryService.findById(id);
		SearchHistory newSearch = new SearchHistory();
		boolean flag = false;
		if (productId!=searchHistory.getProductId()){
			newSearch.setProductId(productId);
			flag = true;
		}else {
			newSearch.setProductId(searchHistory.getProductId());
		}
		if(searchName != searchHistory.getSearchName()){
			newSearch.setSearchName(searchName);
			flag = true;
		}else{
			newSearch.setSearchName(searchHistory.getSearchName());
		}
		if(component != searchHistory.getComponentId()){
			newSearch.setComponentId(component);
			flag = true;
		}else{
			newSearch.setComponentId(searchHistory.getComponentId());
		}
		if(status != searchHistory.getStatusId()){
			newSearch.setStatusId(status);
			flag = true;
		}else {
			newSearch.setStatusId(searchHistory.getStatusId());
		}
		if(assigned != searchHistory.getAssigned()){
			newSearch.setAssigned(assigned);
			flag = true;
		}else
		{
			newSearch.setAssigned(searchHistory.getAssigned());
		}
		if(severity != searchHistory.getSeverityId()){
			newSearch.setSeverityId(severity);
			flag = true;
		}else{
			newSearch.setSeverityId(searchHistory.getSeverityId());
		}
		if (tag != searchHistory.getTags()){
			newSearch.setTags(tag);
			flag = true;
		}else {
			newSearch.setTags(searchHistory.getTags());
		}
		if(filedBy != searchHistory.getFiledBy()) {
			newSearch.setFiledBy(filedBy);
			flag = true;
		}else {
			newSearch.setFiledBy( searchHistory.getFiledBy());
		}
        if(startTime != null){
            Timestamp time = Timestamp.valueOf(LocalDateTime.parse(startTime));
            if(time!= searchHistory.getStartTime()) {
                newSearch.setStartTime(time);
                flag = true;
            }else {
                newSearch.setStartTime( searchHistory.getStartTime());
            }
        }
        if(endTime != null){
            Timestamp time = Timestamp.valueOf(LocalDateTime.parse(endTime));
            if(time!= searchHistory.getEndTime()) {
                newSearch.setEndTime(time);
                flag = true;
            }else {
                newSearch.setEndTime( searchHistory.getEndTime());
            }
        }

		if(flag){
			newSearch.setCreatTime(Timestamp.valueOf(LocalDateTime.now()));
			SearchHistory result = iSearchHistoryService.save(newSearch);
			return gson.toJson(result);
		}else {
			return null;
		}



	}

	@RequestMapping(value ="advancedSearch",params = {"productId","component","status","assigned","severity","tag","filedBy","startTime","endTime"}, method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody String advancedSearch(HttpServletRequest request,@RequestParam Integer productId,
			@RequestParam Integer component,@RequestParam Integer status,@RequestParam Integer assigned,
			@RequestParam Integer severity,@RequestParam String tag,@RequestParam Integer filedBy,@RequestParam String startTime,@RequestParam String endTime) {
		System.out.println(startTime);
		LocalDateTime stl=LocalDateTime.parse(startTime);
		LocalDateTime etl=LocalDateTime.parse(endTime);
		Timestamp st=Timestamp.valueOf(stl);
		Timestamp et=Timestamp.valueOf(etl);
		System.out.println(st);
		System.out.println(et);

		List<Bug> result=bugservice.findByCondition(productId,component,status,assigned,severity,tag,filedBy,st,et);
		Gson gson=new Gson();
		return gson.toJson(result);
	}
	
	@RequestMapping(value ="updateBug",params = {"status","assigned","product","subject","bugNo",
			"comments","userId"}, method = RequestMethod.GET)
	@Transactional
	public @ResponseBody String updateBug(HttpServletRequest request,@RequestParam Integer status,
			@RequestParam Integer assigned,@RequestParam Integer product,@RequestParam String subject,
			@RequestParam Integer bugNo,@RequestParam String comments,@RequestParam Integer userId) {
		
		
		//插入updates
		Bug bug=bugservice.findByBugNo(bugNo);
		Updates update=new Updates();
		update.setComments(comments);
		update.setBugId(bugNo);
		update.setUserId(userId);
		update.setTime(Timestamp.valueOf(LocalDateTime.now()));
		
		StringBuilder chg=new StringBuilder("");
		if(bug.getStatusId()!=status){chg.append("CHG:status-").append(utilservice.findStatusById(bug.getStatusId()).getDescription())
			.append(" to ").append(utilservice.findStatusById(status).getDescription()+";");}	
		if(bug.getAssigned()!=assigned){chg.append("CHG:assigned-").append(userservice.findById(bug.getAssigned()).getUserName())
			.append(" to ").append(userservice.findById(assigned).getUserName()+";");}
		if(bug.getAssigned()!=assigned){
			String recipient=userservice.findById(assigned).getEmail();
		new mailtest().send(bugNo, recipient);}
		if(bug.getProductId()!=product){chg.append("CHG:product-").append(utilservice.findProductById(bug.getProductId()).getDescription())
			.append(" to ").append(utilservice.findProductById(product).getDescription()+";");}
		if(!bug.getSubject().equals(subject)){chg.append("CHG:subject-").append(bug.getSubject())
			.append(" to ").append(subject+";");}
		
		update.setChg(chg.toString());
		Updates result2=updateservice.save(update);
		int result=bugservice.updateBug(status, assigned, product, subject, bugNo);


		EsUpdates esUpdates =  iUpdatesService.save(new EsUpdates(update));
		bug.setStatusId(status);
		bug.setAssigned(assigned);
		bug.setProductId(product);
		bug.setSubject(subject);
		EsBug esBug = iBugService.update(new EsBug(bug));
		
		Gson gson=new Gson();
		
		return result+"  "+gson.toJson(result2);
	}
	
}
