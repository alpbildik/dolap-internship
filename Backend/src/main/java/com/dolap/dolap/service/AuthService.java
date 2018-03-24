package com.dolap.dolap.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dolap.dolap.entities.Token;
import com.dolap.dolap.entities.Product;

import com.dolap.dolap.repo.TokenRepository;

@Service
public class AuthService {

	private static TokenRepository tokenRepository;
	
	@Autowired
	private TokenRepository tRepository;
	
	@PostConstruct
	public void init() {
		tokenRepository = tRepository;
	}
	
	public static boolean isRegistered(String token){
	    Token t = tokenRepository.findByToken(token);
	    if (t != null){
	    	return true;
	    }
		return false;
	}
	
	public static boolean isOwner(String token, Product product){
		Token t = tokenRepository.findByToken(token);
	    if (t != null && t.getUser().getEmail() == product.getCreator().getEmail() ){
	    	return true;
	    }
		return false;
	}
}
