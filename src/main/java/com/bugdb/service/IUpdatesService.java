package com.bugdb.service;

import com.bugdb.domain.EsUpdates;

/**
 * Created by jingwei on 06/04/2017.
 */
public interface IUpdatesService {
    EsUpdates save(EsUpdates esUpdates);

    Iterable<EsUpdates> saveAll(Iterable<EsUpdates> updates);

    Iterable<EsUpdates> search(String str);

    Iterable<EsUpdates> findAll();

    void deleteAll();
}
