package com.inventory.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Proxy;

import com.inventory.DTO.AdminDTO;


@Entity
@Table(name = "Customer")
@JsonIgnoreProperties(ignoreUnknown = true)
@Proxy(lazy=false)
public class Customer implements Serializable{

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getContactNo() {
		return contactNo;
	}

	public void setContactNo(long contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "contactNo")
	private long contactNo;
	
	@Column(name = "email")
	private String email;

	@ManyToOne
	@JoinColumn(name="adminId")
	private Admin admin;

	@OneToOne(mappedBy = "customer", fetch=FetchType.EAGER)
	private SaleInvoice saleInvoice;
	
	@OneToOne(mappedBy = "customer", fetch=FetchType.EAGER)
	private CustomerReport customerReport;
	
	public CustomerReport getCustomerReport() {
		return customerReport;
	}

	public void setCustomerReport(CustomerReport customerReport) {
		this.customerReport = customerReport;
	}

	public SaleInvoice getSaleInvoice() {
		return saleInvoice;
	}

	public void setSaleInvoice(SaleInvoice saleInvoice) {
		this.saleInvoice = saleInvoice;
	}

	public Customer(){
		
	}
	
	public Customer(AdminDTO adminDTO){
		this.name = adminDTO.getName();
		this.address = adminDTO.getAddress();
		this.contactNo = adminDTO.getContactNo();
		this.email = adminDTO.getEmail();
	}
}
