package com.bugdb.service.impl;

import javax.transaction.Transactional;

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bugdb.domain.Bug;
import com.bugdb.domain.EsBug;
import com.bugdb.repository.BugRepository;
import com.bugdb.repository.EsBugRepository;
import com.bugdb.service.IBugService;

/**
 * Created by jingwei on 2017/4/4.
 */
@Service
public class BugServiceImpl implements IBugService {
    @Autowired
    private EsBugRepository esBugRepository;
    @Autowired
    private BugRepository bugRepository;

    @Override
    public EsBug findByBugNo(int bugNo){
        return esBugRepository.findByBugNo(bugNo);
    }

    public Iterable<Bug> findAll(){
        return bugRepository.findAll();
    }

    @Override
    @Transactional
    public EsBug save(EsBug esBug) {
        return esBugRepository.save(esBug);
    }

    @Override
    public Iterable<EsBug> search(String str) {
        QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder("*"+str+"*");
       // MatchAllDocsQuery matchAllDocsQuery = new MatchAllDocsQuery();

        Iterable<EsBug> bugList = esBugRepository.search(queryBuilder);
        return bugList;

    }

    @Override
    public Iterable<EsBug> findAllEsBug() {
        return esBugRepository.findAll();
    }

    @Override
    @Transactional
    public Iterable<EsBug> deleteAll() {
        esBugRepository.deleteAll();
        return esBugRepository.findAll();
    }

    @Override
    public EsBug update(EsBug esBug) {
        esBugRepository.delete(esBug.getBugNo());
        esBugRepository.save(esBug);
        return null;
    }


}
