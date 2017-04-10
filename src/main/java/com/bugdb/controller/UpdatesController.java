package com.bugdb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bugdb.domain.Updates;
import com.bugdb.service.UpdatesService;
import com.google.gson.Gson;
@Controller
@RequestMapping("/")
public class UpdatesController {
	
	@Autowired
	private UpdatesService updatesservice;

	@RequestMapping(value ="findUpdatesByBugNo",params = {"bugNo"}, method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody String findUpdatesByBugNo(HttpServletRequest request,@RequestParam int bugNo) {		
		List<Updates> result;
		result=updatesservice.findByBugId(bugNo);
		Gson gson=new Gson();
		return gson.toJson(result);
		}
	}
