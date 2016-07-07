package com.inventory.dao;

import java.util.List;

import com.inventory.model.TaxInvoice;

public interface TaxInvoiceDao {

	boolean addOrUpdateTaxInvoice(TaxInvoice taxInvoice);
	List<TaxInvoice> getTaxInvoiceList();
}
