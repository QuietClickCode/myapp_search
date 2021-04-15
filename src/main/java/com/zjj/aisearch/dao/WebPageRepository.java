package com.zjj.aisearch.dao;

import com.zjj.aisearch.model.WebPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WebPageRepository extends JpaRepository<WebPage, Integer> {

}
