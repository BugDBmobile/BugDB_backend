package com.bugdb.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bugdb.domain.Component;
import com.bugdb.domain.Os;
import com.bugdb.domain.Product;
import com.bugdb.domain.Severity;
import com.bugdb.domain.Status;
import com.bugdb.repository.ComponentRepository;
import com.bugdb.repository.OsRepository;
import com.bugdb.repository.ProductRepository;
import com.bugdb.repository.SeverityRepository;
import com.bugdb.repository.StatusRepository;


@Service
public class UtilService {
	@Autowired
	private ComponentRepository cr;
	@Autowired
	private OsRepository or;
	@Autowired
	private ProductRepository pr;
	@Autowired
	private SeverityRepository sr;
	@Autowired
	private StatusRepository str;
	
	@Transactional
	public List<Component> findAllComponent(){
		return cr.findAll();
	}
	
	@Transactional
	public List<Os> findAllOs(){
		return or.findAll();
	}

	@Transactional
	public List<Product> findAllProduct(){
		return pr.findAll();
	}
	
	@Transactional
	public Product findProductById(int id){
		return pr.findById(id);
	}
	
	@Transactional
	public List<Severity> findAllSeverity(){
		return sr.findAll();
	}
	
	@Transactional
	public List<Status> findAllStatus(){
		return str.findAll();
	}
	
	@Transactional 
	public Status findStatusById(int id){
		return str.findById(id);
	}
}
