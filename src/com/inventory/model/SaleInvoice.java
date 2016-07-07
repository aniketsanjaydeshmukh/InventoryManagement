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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Proxy;


@Entity
@Table(name = "SaleInvoice")
@JsonIgnoreProperties(ignoreUnknown = true)
@Proxy(lazy=false)
public class SaleInvoice implements Serializable{

	@Id
	@GeneratedValue
	@Column(name = "saleInvoiceNo")
	private long saleInvoiceNo;
	
	@Column(name = "invoiceDate")
	private String invoiceDate;
	
	@Column(name = "quantity")
	private long quantity;
	
	@Column(name = "discountedAmount")
	private double discountedAmount;
	
	@Column(name = "finalAmount")
	private double finalAmount;
	
	@Column(name = "paymentMode")
	private String paymentMode;
	
	@OneToOne()
    @JoinColumn(name="checkerId")
	private Checker checker;
	
	
	public Checker getChecker() {
		return checker;
	}

	public void setChecker(Checker checker) {
		this.checker = checker;
	}

	@OneToOne()
    @JoinColumn(name="salesPersonId")
	private SalesPerson salesPerson;
	
	
	public SalesPerson getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(SalesPerson salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	@OneToOne()
    @JoinColumn(name="productId")
	private Product product;

	@OneToOne
	@JoinColumn(name = "customerId")
	private Customer customer;

	@OneToOne(mappedBy = "saleInvoice", fetch=FetchType.EAGER)
	private TaxInvoice taxInvoice;
	
	@OneToOne(mappedBy = "saleInvoice", fetch=FetchType.EAGER)
	private CustomerReport customerReport;
	

	public CustomerReport getCustomerReport() {
		return customerReport;
	}

	public void setCustomerReport(CustomerReport customerReport) {
		this.customerReport = customerReport;
	}
	
	public TaxInvoice getTaxInvoice() {
		return taxInvoice;
	}

	public void setTaxInvoice(TaxInvoice taxInvoice) {
		this.taxInvoice = taxInvoice;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public long getSaleInvoiceNo() {
		return saleInvoiceNo;
	}

	public void setSaleInvoiceNo(long saleInvoiceNo) {
		this.saleInvoiceNo = saleInvoiceNo;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public double getDiscountedAmount() {
		return discountedAmount;
	}

	public void setDiscountedAmount(double discountedAmount) {
		this.discountedAmount = discountedAmount;
	}

	public double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(double finalAmount) {
		this.finalAmount = finalAmount;
	}




	
}
