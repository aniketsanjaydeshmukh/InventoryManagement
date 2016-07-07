package com.inventory.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.model.SupplierReport;
import com.inventory.model.TaxInvoice;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
public class TaxInvoiceDaoImpl implements TaxInvoiceDao{

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;
	
	@Override
	public boolean addOrUpdateTaxInvoice(TaxInvoice taxInvoice) {
		boolean flag = false;
	    try{    
	    	session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(taxInvoice);
			tx.commit();
			session.close();
			flag = true;
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return flag;	
	}

	@Override
	public List<TaxInvoice> getTaxInvoiceList() {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		
		Criteria c = session.createCriteria(TaxInvoice.class);

		List<TaxInvoice> taxInvoiceList = c.list();
		tx.commit();
		session.close();
		return taxInvoiceList;

	}
}
