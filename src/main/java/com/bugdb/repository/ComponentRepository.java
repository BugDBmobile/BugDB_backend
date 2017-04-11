package com.bugdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bugdb.domain.Component;


public interface ComponentRepository extends JpaRepository<Component, Integer>, JpaSpecificationExecutor<Component> {

    public Component findById(Integer id);
}