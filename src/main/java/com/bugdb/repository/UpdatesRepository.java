package com.bugdb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.bugdb.domain.Updates;


public interface UpdatesRepository extends JpaRepository<Updates, Long>, JpaSpecificationExecutor<Updates> {
	@Query("select u from Updates u where bugId = ? order by u.time")
	public List<Updates> findByBugId(int bugId);
}
