package com.bugdb.service.impl;

import javax.transaction.Transactional;

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bugdb.domain.EsUpdates;
import com.bugdb.repository.EsUpdatesRepository;
import com.bugdb.service.IUpdatesService;

/**
 * Created by jingwei on 06/04/2017.
 */
@Service
public class UpdatesServiceImpl implements IUpdatesService {

    @Autowired
    private EsUpdatesRepository esUpdatesRepository;
    @Override
    @Transactional
    public EsUpdates save(EsUpdates esUpdates) {
        return esUpdatesRepository.save(esUpdates);
    }

    @Override
    @Transactional
    public Iterable<EsUpdates> saveAll(Iterable<EsUpdates> updates) {
        return esUpdatesRepository.save(updates);
    }


    @Override
    public Iterable<EsUpdates> search(String str) {
        QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder("*"+str+"*");
        Iterable<EsUpdates> updatesList = esUpdatesRepository.search(queryBuilder);
        return updatesList;
    }

    @Override
    public Iterable<EsUpdates> findAll() {
        return esUpdatesRepository.findAll();

    }

    @Override
    public void deleteAll() {
        esUpdatesRepository.deleteAll();
    }


}
