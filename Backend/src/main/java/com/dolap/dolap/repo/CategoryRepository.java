package com.dolap.dolap.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dolap.dolap.entities.Product;
import com.dolap.dolap.entities.Category;
import com.dolap.dolap.entities.Image;



public interface CategoryRepository extends JpaRepository<Category, Long> {
	  @Query("select c from Category c where c.id = ?1")
	  Category findById(Integer id);
	  
	  @Query("select c from Category c where c.parentId = ?1")
	  List <Category> findByParentId(Integer parentId);
}