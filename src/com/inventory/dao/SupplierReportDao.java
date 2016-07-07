package com.inventory.dao;

import java.util.List;

import com.inventory.model.SupplierReport;

public interface SupplierReportDao {

	boolean createOrUpdateSupplierReport(SupplierReport supplierReport);
	SupplierReport getSupplierReportBySupplierId(long supplierId);
	List<SupplierReport> getSupplierReportList();
}
