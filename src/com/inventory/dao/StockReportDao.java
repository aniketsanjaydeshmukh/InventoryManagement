package com.inventory.dao;

import java.util.List;

import com.inventory.model.StockReport;

public interface StockReportDao {
	boolean createOrUpdateStockReport(StockReport stockReport);
	StockReport getStockReportByProductId(long productId);
	List<StockReport> getStockReportList();
}
