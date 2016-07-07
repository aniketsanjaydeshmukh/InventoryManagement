package com.inventory.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.dao.StockReportDao;
import com.inventory.model.StockReport;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
@Service("stockReportServices")
public class StockReportServicesImpl implements StockReportServices{

	@Autowired
	StockReportDao stockReportDao;
	
	@Override
	public boolean createOrUpdateStockReport(StockReport stockReport) {
		return stockReportDao.createOrUpdateStockReport(stockReport);
	}

	@Override
	public StockReport getStockReportByProductId(long productId) {
		return stockReportDao.getStockReportByProductId(productId);
	}

	@Override
	public List<StockReport> getStockReportList() {
		return stockReportDao.getStockReportList();
	}

}
