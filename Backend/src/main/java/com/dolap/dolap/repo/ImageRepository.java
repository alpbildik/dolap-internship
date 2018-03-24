package com.dolap.dolap.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dolap.dolap.entities.Product;
import com.dolap.dolap.entities.Image;



public interface ImageRepository extends JpaRepository<Image, Long> {
	  @Query("select p from Image p where p.product = ?1")
	  List <Image> findByProduct(Product product);
}