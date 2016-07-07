package com.inventory.dao;

import java.util.List;

import com.inventory.model.SaleInvoice;

public interface SaleInvoiceDao {

	boolean addOrUpdateSaleInvoice(SaleInvoice saleInvoice);
	List<SaleInvoice> getSaleInvoiceList();
}
