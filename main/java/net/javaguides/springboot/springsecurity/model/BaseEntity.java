package net.javaguides.springboot.springsecurity.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;



@MappedSuperclass
public abstract class BaseEntity {

		  
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BaseEntity(Long id) {
		super();
		this.id = id;
	}

	public BaseEntity() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
