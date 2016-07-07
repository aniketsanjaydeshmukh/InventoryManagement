package com.inventory.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.model.StockReport;
import com.inventory.model.SupplierReport;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
public class SupplierReportDaoImpl implements SupplierReportDao{


	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;
	
	@Override
	public boolean createOrUpdateSupplierReport(SupplierReport supplierReport) {
		boolean flag = false;
	    try{    
	    	session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(supplierReport);
			tx.commit();
			session.close();
			flag = true;
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return flag;	

	}

	@Override
	public SupplierReport getSupplierReportBySupplierId(long supplierId) {
		SupplierReport stockReport = new SupplierReport();
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		
		Criteria c = session.createCriteria(SupplierReport.class);
		c.createAlias("supplier", "s");
		c.add(Restrictions.eq("s.id", supplierId));
		
		Object result=c.uniqueResult();
		stockReport = (SupplierReport)result;

			tx.commit();
			session.close();
			
		return stockReport;

	}

	@Override
	public List<SupplierReport> getSupplierReportList() {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		
		Criteria c = session.createCriteria(SupplierReport.class);

		List<SupplierReport> supplierReportList = c.list();
		tx.commit();
		session.close();
		return supplierReportList;
	}

	
}
