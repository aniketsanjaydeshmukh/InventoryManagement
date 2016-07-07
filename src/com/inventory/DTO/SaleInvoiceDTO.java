package com.inventory.DTO;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleInvoiceDTO {
	private long saleInvoiceNo;
	private String saleInvoiceDate;
	private long productId;
	private long customerId;
	private String sessionId;
	private long quantity;
	private String paymentMode;
	
	public long getSaleInvoiceNo() {
		return saleInvoiceNo;
	}
	public void setSaleInvoiceNo(long saleInvoiceNo) {
		this.saleInvoiceNo = saleInvoiceNo;
	}
	public String getSaleInvoiceDate() {
		return saleInvoiceDate;
	}
	public void setSaleInvoiceDate(String saleInvoiceDate) {
		this.saleInvoiceDate = saleInvoiceDate;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	
}
