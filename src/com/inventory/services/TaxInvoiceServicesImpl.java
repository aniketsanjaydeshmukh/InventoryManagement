package com.inventory.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.dao.TaxInvoiceDao;
import com.inventory.model.TaxInvoice;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
@Service("taxInvoiceServices")
public class TaxInvoiceServicesImpl implements TaxInvoiceServices{

	@Autowired
	TaxInvoiceDao taxInvoiceDao;

	@Override
	public boolean addOrUpdateTaxInvoice(TaxInvoice taxInvoice) {
		return taxInvoiceDao.addOrUpdateTaxInvoice(taxInvoice);
	}

	@Override
	public List<TaxInvoice> getTaxInvoiceList() {
		return taxInvoiceDao.getTaxInvoiceList();
	}
}
