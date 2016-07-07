package com.inventory.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.dao.PurchaseInvoiceDao;
import com.inventory.model.PurchaseInvoice;


@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
@Service("purchaseInvoiceServices")
public class PurchaseInvoiceServicesImpl implements PurchaseInvoiceServices{

	@Autowired
	PurchaseInvoiceDao purchaseInvoiceDao;
	
	@Override
	public boolean addOrUpdatePurchaseInvoice(PurchaseInvoice purchaseInvoice) {
		return purchaseInvoiceDao.addOrUpdatePurchaseInvoice(purchaseInvoice);
	}

	@Override
	public PurchaseInvoice getPurchaseInvoiceById(long purchaseInvoiceId) {
		return purchaseInvoiceDao.getPurchaseInvoiceById(purchaseInvoiceId);
	}

	@Override
	public List<PurchaseInvoice> getPurchaseList() {
		return purchaseInvoiceDao.getPurchaseList();
	}

	@Override
	public boolean deletePurchaseInvoice(long purchaseInvoiceNo) {
		return purchaseInvoiceDao.deletePurchaseInvoice(purchaseInvoiceNo);
	}

	@Override
	public PurchaseInvoice verifyPurchaseInvoice(long purchaseInvoiceNo, boolean verify) {
		return purchaseInvoiceDao.verifyPurchaseInvoice(purchaseInvoiceNo, verify);
	}

	@Override
	public PurchaseInvoice getPurchaseInvoiceBySupplierReportId(
			long supplierReportId) {
		return purchaseInvoiceDao.getPurchaseInvoiceBySupplierReportId(supplierReportId);
	}

}
