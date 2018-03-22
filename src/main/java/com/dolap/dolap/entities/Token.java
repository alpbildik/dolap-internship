package com.dolap.dolap.entities;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import com.dolap.dolap.entities.User;

@Entity 
public class Token {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(unique=true)
    private String token;
    
    @OneToOne(cascade=CascadeType.REMOVE)
    private User user;
   
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setToken(String token){
		this.token = token;
	}
	
	public String getToken(){
		return this.token;
	}

	public void setUser(User user){
		this.user = user;
	}
	
	public User getUser(){
		return this.user;
	}
}
