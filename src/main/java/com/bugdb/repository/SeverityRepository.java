package com.bugdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bugdb.domain.Severity;


public interface SeverityRepository extends JpaRepository<Severity, Long>, JpaSpecificationExecutor<Severity> {
}
