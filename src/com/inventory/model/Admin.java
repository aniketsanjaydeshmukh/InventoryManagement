package com.inventory.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Proxy;

import com.inventory.DTO.AdminDTO;

@Entity
@Table(name = "Admin")
@JsonIgnoreProperties(ignoreUnknown = true)
@Proxy(lazy=false)
public class Admin implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "userRole")
	private String userRole;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "address")
	private String address;

	@Column(name = "password")
	private String password;
	
	@OneToMany(mappedBy="admin")
	private Set<Checker> checkers;
	
	@OneToMany(mappedBy="admin")
	private Set<Maker> makers;
	
	@OneToMany(mappedBy="admin")
	private Set<Supplier> suppliers;
	
	
	public Set<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(Set<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

	public Set<Maker> getMakers() {
		return makers;
	}

	public void setMakers(Set<Maker> makers) {
		this.makers = makers;
	}

	public Set<Checker> getCheckers() {
		return checkers;
	}

	public void setCheckers(Set<Checker> checkers) {
		this.checkers = checkers;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Admin(){
		
	}
	
	public Admin(AdminDTO adminDTO){
		this.username = adminDTO.getUsername();
		this.userRole = adminDTO.getUserRole();
		this.email = adminDTO.getEmail();
		this.name = adminDTO.getName();
		this.address = adminDTO.getAddress();
		this.password = adminDTO.getPassword();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
