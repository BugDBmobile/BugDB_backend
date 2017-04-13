package com.bugdb.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.sql.Timestamp;
import java.time.*;
import javax.transaction.Transactional;

import com.bugdb.domain.Status;
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
    @Autowired
    private UtilService us;
	
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
	

	public List<Bug> findByCondition(Integer productId,Integer component,Integer status,Integer assigned,Integer severity,String tag,Integer filedBy,Timestamp starttime,Timestamp endtime){
        List<Bug> resultList = null;
        Specification querySpecifi = new Specification<Bug>() {
            @Override
            public Predicate toPredicate(Root<Bug> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        
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
                System.out.println(starttime);
	            System.out.println(endtime);
                if(null != starttime){
                	predicates.add(criteriaBuilder.greaterThan(root.get("filed"), starttime));
                }
                if(null != endtime){
                	predicates.add(criteriaBuilder.lessThan(root.get("filed"), endtime));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        resultList =  br.findAll(querySpecifi);
        return resultList;
    }

    public int countByStartTime(int userId, int isClose, Timestamp startTime, Timestamp endTime){
        List<Bug> resultList = new ArrayList<>();

        List<Status> sts = us.findStatus(isClose);
        for (Status st : sts) {
            Integer i = st.getId();

            Specification querySpecifi = new Specification<Bug>() {
                @Override
                public Predicate toPredicate(Root<Bug> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(criteriaBuilder.equal(root.get("filedBy"),userId));

                    predicates.add(criteriaBuilder.equal(root.get("statusId"),i));
                    System.out.println(startTime);
                    if(null != startTime){
                        predicates.add(criteriaBuilder.greaterThan(root.get("filed"), startTime));
                    }

                    predicates.add(criteriaBuilder.lessThan(root.get("filed"), endTime));

                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            };
            resultList.addAll(br.findAll(querySpecifi));
        }
        return resultList.size();
    }

}
