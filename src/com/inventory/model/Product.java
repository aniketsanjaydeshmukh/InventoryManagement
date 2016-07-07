package com.inventory.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Proxy;

import com.inventory.DTO.ProductDTO;

@Entity
@Table(name = "Product")
@JsonIgnoreProperties(ignoreUnknown = true)
@Proxy(lazy=false)
public class Product implements Serializable{
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column (name = "productName")
	private String productName;
	
	@Column (name = "modelNumber")
	private String modelNumber;
	
	@Column (name = "productType")
	private String productType;
	
	@Column(name = "quantity")
	private long quantity;
	
	@Column(name = "unitPrice")
	private double unitPrice;
	
	@Column(name = "discountRate")
	private double discountRate;
	
	@OneToMany(mappedBy="product")	
	private Set<PurchaseInvoice> purchaseInvoice;
	
	@OneToOne(mappedBy="product" , fetch = FetchType.EAGER)
	private StockReport stockReport; 
	
	@OneToOne(mappedBy = "product", fetch=FetchType.EAGER)
	private SaleInvoice saleInvoice;
	
	public SaleInvoice getSaleInvoice() {
		return saleInvoice;
	}

	public void setSaleInvoice(SaleInvoice saleInvoice) {
		this.saleInvoice = saleInvoice;
	}

	public StockReport getStockReport() {
		return stockReport;
	}

	public void setStockReport(StockReport stockReport) {
		this.stockReport = stockReport;
	}

	public Product(){
		
	}
	
	public Product(ProductDTO productDTO){
		this.productName = productDTO.getProductName();
		this.modelNumber = productDTO.getModelNumber();
		this.productType = productDTO.getProductType();
		this.quantity = productDTO.getQuantity();
		this.unitPrice = productDTO.getUnitPrice();
		this.discountRate = productDTO.getDiscountRate();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
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

	public double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	public Set<PurchaseInvoice> getPurchaseInvoice() {
		return purchaseInvoice;
	}

	public void setPurchaseInvoice(Set<PurchaseInvoice> purchaseInvoice) {
		this.purchaseInvoice = purchaseInvoice;
	}

}
