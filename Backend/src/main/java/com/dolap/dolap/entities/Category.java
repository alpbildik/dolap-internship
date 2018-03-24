package com.dolap.dolap.entities;


import javax.persistence.Entity;
import javax.persistence.Id;


@Entity 
public class Category {
    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String name;

    private Integer parentId;

    public Category() {
		// TODO Auto-generated constructor stub
	}
    
    public Category(Integer id) {
    	this.id = id;
    }
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
}
