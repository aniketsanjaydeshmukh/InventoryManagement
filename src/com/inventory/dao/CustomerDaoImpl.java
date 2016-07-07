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

import com.inventory.model.Customer;
import com.inventory.model.Supplier;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
public class CustomerDaoImpl implements CustomerDao{

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;
	
	@Override
	public boolean addOrUpdateCustomer(Customer customer) {
		boolean flag = false;
	    try{    
	    	session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(customer);
			tx.commit();
			session.close();
			flag = true;
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return flag;
	}

	@Override
	public boolean deleteCustomer(long customerId) {
		boolean flag = true;
		try{
		session = sessionFactory.openSession();
		Object o = session.load(Customer.class, customerId);
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
	public List<Customer> customerList(long adminId) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		
		Criteria c = session.createCriteria(Customer.class);
		c.createAlias("admin", "a");
		c.add(Restrictions.eq("a.id", adminId));
		
		List<Customer> customerList = c.list();
		tx.commit();
		session.close();
		return customerList;
	}

	@Override
	public Customer getCustomerById(long id) {
		Session session;
		Customer  customer = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Customer.class);
			 criteria.add(Restrictions.eq("id", id));
			 Object result=criteria.uniqueResult();
			 customer = (Customer)result;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return customer;
	}

}
