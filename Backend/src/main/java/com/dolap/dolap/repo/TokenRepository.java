package com.dolap.dolap.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dolap.dolap.entities.Token;
import com.dolap.dolap.entities.User;

public interface TokenRepository extends JpaRepository<Token, Long> {
	  @Query("select t from Token t where t.user = ?1")
	  Token findByUser(User user);
	  
	  @Query("select t from Token t where t.token = ?1")
	  Token findByToken(String token);
}