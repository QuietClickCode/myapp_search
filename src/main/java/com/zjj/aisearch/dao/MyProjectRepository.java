package com.zjj.aisearch.dao;

import com.zjj.aisearch.model.MyProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MyProjectRepository extends JpaRepository<MyProject, Integer> {

}
