package com.zjj.aisearch.repository;

import com.zjj.aisearch.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface BookmarkRepository extends Repository<Bookmark,Long> {

    Page<Bookmark> findByTitle(String title, Pageable pageable);


}
