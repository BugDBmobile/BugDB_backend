package com.bugdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.bugdb.domain.Status;

import java.util.List;


public interface StatusRepository extends JpaRepository<Status, Long>, JpaSpecificationExecutor<Status> {
	@Query("select s from Status s where id= ?")
	public Status findById(int id);

	public List<Status> findByIsClose(int isClose);
}
