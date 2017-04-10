package com.bugdb.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bugdb.domain.EsUpdates;

/**
 * Created by jingwei on 06/04/2017.
 */
public interface EsUpdatesRepository extends ElasticsearchRepository<EsUpdates, Integer> {


}