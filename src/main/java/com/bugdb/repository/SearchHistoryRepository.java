package com.bugdb.repository;

import com.bugdb.domain.Product;
import com.bugdb.domain.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jingwei on 13/04/2017.
 */

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Integer>, JpaSpecificationExecutor<SearchHistory> {
    @Query("select b from SearchHistory b where b.searchUserId= ?")
	public List<SearchHistory> findBySearchUserId(Integer id);
    @Query("select b from SearchHistory b where b.id= ?")
   	public SearchHistory findById(Integer id);
}
