package com.bugdb.service.impl;

import com.bugdb.domain.SearchHistory;
import com.bugdb.repository.SearchHistoryRepository;
import com.bugdb.service.ISearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jingwei on 13/04/2017.
 */
@Service
public class SearchHistroyServiceImp implements ISearchHistoryService {
    @Autowired
    private SearchHistoryRepository shr;
    @Override
    public List<SearchHistory> findBySearchUserId(Integer id) {
        return shr.findBySearchUserId(id);
    }

    @Override
    public SearchHistory findById(Integer id) {
        return shr.findById(id);
    }

    @Override
    public SearchHistory save(SearchHistory searchHistory) {
        return shr.save(searchHistory);
    }
}
