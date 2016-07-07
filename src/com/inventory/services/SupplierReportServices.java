package com.inventory.services;

import java.util.List;

import com.inventory.model.SupplierReport;

public interface SupplierReportServices {
	boolean createOrUpdateSupplierReport(SupplierReport supplierReport);
	SupplierReport getSupplierReportBySupplierId(long supplierId);
	List<SupplierReport> getSupplierReportList();
}
