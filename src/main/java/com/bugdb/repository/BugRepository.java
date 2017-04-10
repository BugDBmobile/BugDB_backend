package com.bugdb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bugdb.domain.Bug;
import com.bugdb.domain.User;


public interface BugRepository extends JpaRepository<Bug, Integer>, JpaSpecificationExecutor<User> {
	@Query("select b from Bug b where bugNo = ?")
	public Bug findByBugNo(Integer bugNo);
	
	@Modifying
	@Query("update Bug b set b.statusId=?1,b.assigned=?2,b.productId=?3,b.subject=?4 where b.bugNo=?5")
	public int updateBug(int status,int assigned,int product,String subject,int bugNo);
	
	@Query("select b from Bug b where assigned = ?")
	public List<Bug> findByAssigned(int assigned);
	
	@Query("select b from Bug b where filedBy = ?")
	public List<Bug> findByFiledBy(int filedBy);
}
