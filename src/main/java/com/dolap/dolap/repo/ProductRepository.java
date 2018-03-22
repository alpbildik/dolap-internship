package com.dolap.dolap.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dolap.dolap.entities.Product;
import com.dolap.dolap.entities.User;

public interface ProductRepository extends JpaRepository<Product, Long> {
	  @Query("select p from Product p where p.creator = ?1")
	  List <Product> findByCreator(User creator);
	  
	  @Query("select p from Product p where p.id = ?1")
	  Product findByPID(Integer id);
}