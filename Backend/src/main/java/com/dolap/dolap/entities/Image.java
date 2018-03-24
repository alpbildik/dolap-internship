package com.dolap.dolap.entities;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity 
public class Image {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String path;
    
    @ManyToOne(cascade=CascadeType.ALL)
    private Product product;
  

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Product getProduct() {
		return this.product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
}
