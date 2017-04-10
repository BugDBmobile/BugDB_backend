package com.bugdb.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bugdb.domain.User;
import com.bugdb.service.UserService;
import com.bugdb.vo.ErroInfo;
import com.google.gson.Gson;
@Controller
@RequestMapping("/")
public class UserController {
	
	@Autowired
	private UserService userService;

	@RequestMapping(value ="login",params = {"userName","password"}, method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody String user(HttpServletRequest request,@RequestParam String userName, @RequestParam String password) {		

		User user = userService.findByUserName(userName);
		Gson gson = new Gson();	
		if(user!=null&&password.equals(user.getPassword())){
			
			return gson.toJson(user);
		}
		else{
			ErroInfo ei = new ErroInfo();
			ei.setErrCont("User name or password is wrong");
			return gson.toJson(ei);
		}
	}

	
	
}
