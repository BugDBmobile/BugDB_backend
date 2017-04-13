package com.bugdb.service;

import com.bugdb.domain.SearchHistory;

import java.util.List;

/**
 * Created by jingwei on 13/04/2017.
 */
public interface ISearchHistoryService {
    List<SearchHistory> findBySearchUserId(Integer id);
    SearchHistory findById(Integer id);
    SearchHistory save(SearchHistory searchHistory);
}
