package com.bugdb.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.bugdb.domain.User;
import com.bugdb.repository.UserRepository;


@Service
public class UserService {
	@Autowired
	private UserRepository ur;
	
	@Transactional
	public User save(User user){
		return ur.save(user);
	}
	
	@Transactional
	public List<User> findAll(){
		return ur.findAll();
	}
	
	@Transactional
	public void delete(User user){
		this.ur.delete(user);
	}
	@Transactional
	public User findByUserName(String name){
		return ur.findByUserName(name);
	}
	
	@Transactional
	public User findById(Integer id){
		return ur.findById(id);
	}

    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public User getUser(@NotNull @Valid String username) {
        return ur.findByUserName(username);
    }
    
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public User login(@NotNull @Valid String username, @NotNull @Validated String password) {
        return ur.findByUserName(username);
    }

}
