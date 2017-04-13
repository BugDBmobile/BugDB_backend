package com.bugdb.controller;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
	public @ResponseBody String findByFiledBy(HttpServletRequest request,@RequestParam int userId){
		List<Bug> result=bugservice.findByAssigned(userId);
		Gson gson=new Gson();
		return gson.toJson(result);
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
