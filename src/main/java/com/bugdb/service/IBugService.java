package com.bugdb.service;

import com.bugdb.domain.Bug;
import com.bugdb.domain.EsBug;

/**
 * Created by jingwei on 2017/4/4.
 */
public interface IBugService {

    EsBug findByBugNo(int bugNo);

    Iterable<Bug> findAll();

    EsBug save(EsBug esBug);

    Iterable<EsBug> search(String str);

    Iterable<EsBug> findAllEsBug();

    Iterable<EsBug> deleteAll();

    EsBug update(EsBug esBug);

}
