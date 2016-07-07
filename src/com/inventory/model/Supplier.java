package com.inventory.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Proxy;

import com.inventory.DTO.AdminDTO;

@Entity
@Table(name = "Supplier")
@JsonIgnoreProperties(ignoreUnknown = true)
@Proxy(lazy=false)
public class Supplier implements Serializable{

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

	@OneToOne(mappedBy="supplier" , fetch = FetchType.EAGER)
	private SupplierReport supplierReport;
	
	public Set<PurchaseInvoice> getPurchaseInvoice() {
		return purchaseInvoice;
	}

	public void setPurchaseInvoice(Set<PurchaseInvoice> purchaseInvoice) {
		this.purchaseInvoice = purchaseInvoice;
	}

	@OneToMany(mappedBy="product")	
	private Set<PurchaseInvoice> purchaseInvoice;
	
	
	public Supplier(){
		
	}
	
	public Supplier(AdminDTO adminDTO){
		this.name = adminDTO.getName();
		this.address = adminDTO.getAddress();
		this.contactNo = adminDTO.getContactNo();
		this.email = adminDTO.getEmail();
	}

	
	public SupplierReport getSupplierReport() {
		return supplierReport;
	}

	public void setSupplierReport(SupplierReport supplierReport) {
		this.supplierReport = supplierReport;
	}
}