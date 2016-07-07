package com.inventory.services;

import java.util.List;

import com.inventory.model.PurchaseInvoice;

public interface PurchaseInvoiceServices {
	boolean addOrUpdatePurchaseInvoice(PurchaseInvoice purchaseInvoice);
	PurchaseInvoice getPurchaseInvoiceById(long purchaseInvoiceId);
	List<PurchaseInvoice> getPurchaseList();
	boolean deletePurchaseInvoice(long purchaseInvoiceNo);
	PurchaseInvoice verifyPurchaseInvoice(long purchaseInvoiceNo,boolean verify);
	PurchaseInvoice getPurchaseInvoiceBySupplierReportId(long supplierReportId);
}
