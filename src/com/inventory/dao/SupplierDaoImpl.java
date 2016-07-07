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


import com.inventory.model.Supplier;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
public class SupplierDaoImpl implements SupplierDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;
	
	@Override
	public boolean addOrUpdateSupplier(Supplier supplier) {
		boolean flag = false;
	    try{    
	    	session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(supplier);
			tx.commit();
			session.close();
			flag = true;
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return flag;	
	}

	@Override
	public boolean deleteSupplier(long supplierId) {
		boolean flag = true;
		try{
		session = sessionFactory.openSession();
		Object o = session.load(Supplier.class, supplierId);
		tx = session.getTransaction();
		session.beginTransaction();
		session.delete(o);
		tx.commit();
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public List<Supplier> supplierList(long adminId) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		
		Criteria c = session.createCriteria(Supplier.class);
		c.createAlias("admin", "a");
		c.add(Restrictions.eq("a.id", adminId));
		
		List<Supplier> supplierList = c.list();
		tx.commit();
		session.close();
		return supplierList;
	}

	@Override
	public Supplier getSupplierById(long id) {
		Session session;
		Supplier  supplier = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Supplier.class);
			 criteria.add(Restrictions.eq("id", id));
			 Object result=criteria.uniqueResult();
			 supplier = (Supplier)result;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return supplier;
	}

}
