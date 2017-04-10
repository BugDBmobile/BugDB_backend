package com.bugdb.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bugdb.domain.EsBug;

/**
 * Created by jingwei on 2017/4/4.
 */
public interface EsBugRepository extends ElasticsearchRepository<EsBug, Integer> {

    EsBug findByBugNo(int bugNo);
}
