package com.assettrader.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USER_PROFILE")
public class UserProfile extends Person {

	@Id
	@Column(name = "USER_PROFILE_ID")
	@GeneratedValue
	private Long id;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "DELETE_DATE")
	private Date deletedDate;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	// TODO, add "FAVORITE COLUMN"

	// non-owning-side, requires mappedby field name and cascade all
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userProfile")
	private List<Address> address = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

}
