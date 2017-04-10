package com.bugdb.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bugdb.domain.Bug;
import com.bugdb.domain.User;
import com.bugdb.repository.BugRepository;


@Service
public class BugServiceDB {
	@Autowired
	private BugRepository br;
	
	@Transactional
	public Bug findByBugNo(Integer bugNo){
		return br.findByBugNo(bugNo);	
	}
	
	@Transactional
	public int updateBug(int status,int assigned,int product,String subject,int bugNo){
		return br.updateBug(status, assigned, product, subject, bugNo);
	}
	
	@Transactional
	public List<Bug> findByAssigned(int assigned){
		return br.findByAssigned(assigned);
	}
	
	@Transactional
	public List<Bug> findByfiledBy(int filedBy){
		return br.findByFiledBy(filedBy);
	}
	
	@Transactional
	public List<Bug> findByCondition(Integer productId,Integer component,Integer status,Integer assigned,Integer severity,String tag,Integer filedBy){
        List<Bug> resultList = null;
        Specification querySpecifi = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        
                List<Predicate> predicates = new ArrayList<>();
                if(null != productId){
                    predicates.add(criteriaBuilder.equal(root.get("productId"),productId));
                }
                if(null != component ){
                    predicates.add(criteriaBuilder.equal(root.get("componentId"),component));
                }
                if(null != status){
                    predicates.add(criteriaBuilder.equal(root.get("statusId"),status));
                }
                if(null != assigned){
                    predicates.add(criteriaBuilder.equal(root.get("assigned"),assigned));
                }
                if(null != severity){
                    predicates.add(criteriaBuilder.equal(root.get("severityId"),severity));
                }
                if("" != tag){
                    predicates.add(criteriaBuilder.like(root.get("tags"), "%"+tag+"&"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        resultList =  br.findAll(querySpecifi);
        return resultList;
    }

}
