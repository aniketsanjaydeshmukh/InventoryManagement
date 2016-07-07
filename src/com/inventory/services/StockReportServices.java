package com.inventory.services;

import java.util.List;

import com.inventory.model.StockReport;

public interface StockReportServices {
	boolean createOrUpdateStockReport(StockReport stockReport);
	StockReport getStockReportByProductId(long productId);
	List<StockReport> getStockReportList();
}
