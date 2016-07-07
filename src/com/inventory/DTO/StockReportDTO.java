package com.inventory.DTO;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StockReportDTO {

	private long stockReportId;
	private String sessionId;
	private long productId;
	
	public long getStockReportId() {
		return stockReportId;
	}
	public void setStockReportId(long stockReportId) {
		this.stockReportId = stockReportId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
}
