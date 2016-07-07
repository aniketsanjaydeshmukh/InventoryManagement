package com.inventory.dao;

import java.util.List;

import com.inventory.model.Supplier;

public interface SupplierDao {

	boolean addOrUpdateSupplier(Supplier maker);
	boolean deleteSupplier(long checkerId);
	List<Supplier> supplierList(long adminId);
	Supplier getSupplierById(long id);
	
}
