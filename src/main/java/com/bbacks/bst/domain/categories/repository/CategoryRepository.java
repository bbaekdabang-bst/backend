package com.bbacks.bst.domain.categories.repository;

import com.bbacks.bst.domain.categories.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
