package com.inventory.services;

import java.util.List;

import com.inventory.model.TaxInvoice;

public interface TaxInvoiceServices {
	boolean addOrUpdateTaxInvoice(TaxInvoice taxInvoice);
	List<TaxInvoice> getTaxInvoiceList();
}
