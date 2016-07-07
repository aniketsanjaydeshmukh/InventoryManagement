package com.inventory.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.model.CustomerReport;
import com.inventory.model.Product;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
public class CustomerReportDaoImpl implements CustomerReportDao{

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;
	
	@Override
	public boolean addOrUpdateCustomerReport(CustomerReport customerReport) {
		boolean flag = false;
	    try{    
	    	session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(customerReport);
			tx.commit();
			session.close();
			flag = true;
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return flag;	
	}

	@Override
	public List<CustomerReport> getCustomerReportList() {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		
		Criteria c = session.createCriteria(CustomerReport.class);

		List<CustomerReport> customerReportList = c.list();
		tx.commit();
		session.close();
		return customerReportList;
	}
}
