package com.inventory.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "SupplierReport")
@JsonIgnoreProperties(ignoreUnknown = true)
@Proxy(lazy=false)
public class SupplierReport implements Serializable{

	@Id
	@GeneratedValue
	@Column(name = "supplierReportId")
	private long supplierReportId;
	
	@OneToOne()
    @JoinColumn(name="supplierId")
	private Supplier supplier;
	
	@OneToMany(mappedBy="supplierReport")
	private Set<PurchaseInvoice> purchaseInvoice ;

	public long getSupplierReportId() {
		return supplierReportId;
	}

	public void setSupplierReportId(long supplierReportId) {
		this.supplierReportId = supplierReportId;
	}

    
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	
	public Set<PurchaseInvoice> getPurchaseInvoice() {
		return purchaseInvoice;
	}

	public void setPurchaseInvoice(Set<PurchaseInvoice> purchaseInvoice) {
		this.purchaseInvoice = purchaseInvoice;
	}
	
}
