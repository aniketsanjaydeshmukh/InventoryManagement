package com.inventory.services;

import java.util.List;

import com.inventory.model.SaleInvoice;

public interface SaleInvoiceServices {
	boolean addOrUpdateSaleInvoice(SaleInvoice saleInvoice);
	List<SaleInvoice> getSaleInvoiceList();
}
