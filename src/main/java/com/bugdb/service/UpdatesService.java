package com.bugdb.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bugdb.domain.Updates;
import com.bugdb.repository.UpdatesRepository;


@Service
public class UpdatesService {
	@Autowired
	private UpdatesRepository ur;
	
	@Transactional
	public Updates save(Updates update){
		return ur.save(update);
	}
	
	@Transactional
	public List<Updates> findByBugId(int bugId){
		return ur.findByBugId(bugId);
	}

	public List<Updates> findAll(){
		return ur.findAll();
	}

}
