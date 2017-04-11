package com.bugdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bugdb.domain.Os;


public interface OsRepository extends JpaRepository<Os, Integer>, JpaSpecificationExecutor<Os> {
}
