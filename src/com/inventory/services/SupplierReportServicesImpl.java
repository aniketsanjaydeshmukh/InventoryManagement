package com.inventory.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.dao.SupplierReportDao;
import com.inventory.model.SupplierReport;


@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
@Service("supplierReportServices")
public class SupplierReportServicesImpl implements SupplierReportServices{

	@Autowired
	SupplierReportDao supplierReportDao;
	
	@Override
	public boolean createOrUpdateSupplierReport(SupplierReport supplierReport) {
		return supplierReportDao.createOrUpdateSupplierReport(supplierReport);
	}

	@Override
	public SupplierReport getSupplierReportBySupplierId(long supplierId) {
		return supplierReportDao.getSupplierReportBySupplierId(supplierId);
	}

	@Override
	public List<SupplierReport> getSupplierReportList() {
		return supplierReportDao.getSupplierReportList();
	}

}
