package com.inventory.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.dao.SupplierDao;
import com.inventory.model.Supplier;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
@Service("supplierServices")
public class SupplierServicesImpl implements SupplierServices{

	@Autowired
	SupplierDao supplierDao;
	
	@Override
	public boolean addOrUpdateSupplier(Supplier supplier) {
		return supplierDao.addOrUpdateSupplier(supplier);
	}

	@Override
	public boolean deleteSupplier(long supplierId) {
		return supplierDao.deleteSupplier(supplierId); 
	}

	@Override
	public List<Supplier> supplierList(long adminId) {
		return supplierDao.supplierList(adminId);
	}

	@Override
	public Supplier getSupplierById(long id) {
		return supplierDao.getSupplierById(id);
	}

}
