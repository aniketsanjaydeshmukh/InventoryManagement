package com.inventory.DTO;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierReportDTO {

	private long supplierId;
	
	private String sessionId;
	
	private long supplierReportId;
	
	private long productId;

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getSupplierReportId() {
		return supplierReportId;
	}

	public void setSupplierReportId(long supplierReportId) {
		this.supplierReportId = supplierReportId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}
	
}
