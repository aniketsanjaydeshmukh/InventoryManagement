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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Proxy;

import com.inventory.DTO.PurchaseInvoiceDTO;


@Entity
@Table(name = "PurchaseInvoice")
@JsonIgnoreProperties(ignoreUnknown = true)
@Proxy(lazy=false)
public class PurchaseInvoice implements Serializable{

	@Id
	@GeneratedValue
	@Column(name = "invoiceNo")
	private long invoiceNo;
	
	@Column(name = "invoiceDate")
	private String inoviceDate;
	
	@Column(name = "quantity")
	private long quantity;

	@Column(name = "unitPrice")
	private double unitPrice;
	
	@Column(name = "discountAmount")
	private double discountAmount;
	
	@Column(name = "finalAmount")
	private double finalAmount;
	
	@Column(name = "verify")
	private boolean verify = false;

	@ManyToOne
	@JoinColumn(name="supplierReportId")
	private SupplierReport supplierReport;
	
	@OneToOne()
    @JoinColumn(name="makerId")
	private Maker maker;
	
	
	public boolean isVerify() {
		return verify;
	}

	public void setVerify(boolean verify) {
		this.verify = verify;
	}

	@ManyToOne
	@JoinColumn(name="productId")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="supplierId")
	private Supplier supplier;
	
	
	
	public PurchaseInvoice(){
		
	}
	
	public PurchaseInvoice(PurchaseInvoiceDTO purchaseInvoiceDTO){
		this.inoviceDate = purchaseInvoiceDTO.getInvoiceDate();
		this.quantity = purchaseInvoiceDTO.getQuantity();
		this.unitPrice = purchaseInvoiceDTO.getUnitPrice();
	}

	public Maker getMaker() {
		return maker;
	}

	public void setMaker(Maker maker) {
		this.maker = maker;
	}
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public long getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(long invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInoviceDate() {
		return inoviceDate;
	}

	public void setInoviceDate(String inoviceDate) {
		this.inoviceDate = inoviceDate;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(double finalAmount) {
		this.finalAmount = finalAmount;
	}

	
	public SupplierReport getSupplierReport() {
		return supplierReport;
	}

	public void setSupplierReport(SupplierReport supplierReport) {
		this.supplierReport = supplierReport;
	}

}
