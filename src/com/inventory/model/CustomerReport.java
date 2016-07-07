package com.inventory.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Proxy;


@Entity
@Table(name = "CustomerReport")
@JsonIgnoreProperties(ignoreUnknown = true)
@Proxy(lazy=false)
public class CustomerReport implements Serializable{

	@Id
	@GeneratedValue
	@Column(name = "customerReportId")
	private long customerReportId;
	
	@OneToOne()
    @JoinColumn(name="customerId")
	private Customer customer;
	
	@OneToOne()
    @JoinColumn(name="saleInvoiceNo")
	private SaleInvoice saleInvoice;
	
	public long getCustomerReportId() {
		return customerReportId;
	}

	public void setCustomerReportId(long customerReportId) {
		this.customerReportId = customerReportId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public SaleInvoice getSaleInvoice() {
		return saleInvoice;
	}

	public void setSaleInvoice(SaleInvoice saleInvoice) {
		this.saleInvoice = saleInvoice;
	}


	
}
