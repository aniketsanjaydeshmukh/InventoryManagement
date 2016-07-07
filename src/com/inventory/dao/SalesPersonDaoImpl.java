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

import com.inventory.DTO.AdminDTO;
import com.inventory.model.Maker;
import com.inventory.model.SalesPerson;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
public class SalesPersonDaoImpl implements SalesPersonDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;
	
	@Override
	public boolean addOrUpdateSalesPerson(SalesPerson salesPerson) {
		boolean flag = false;
	    try{    
	    	session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(salesPerson);
			tx.commit();
			session.close();
			flag = true;
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return flag;	
	}
	
	@Override
	public boolean checkUnique(String username, String email) {
		 boolean flag=false;
		    try{  	
		    session = sessionFactory.openSession();
			Criteria c = session.createCriteria(SalesPerson.class);
			
			c.add(Restrictions.eq("username", username));
			c.add(Restrictions.eq("email",email));
			Object o = c.uniqueResult();
			
			if(o==null)
			{
				flag=true;
			}
			
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
			return flag;
	}
	
	@Override
	public boolean deleteSalesPerson(long salesPersonId) {
		boolean flag = true;
		try{
		session = sessionFactory.openSession();
		Object o = session.load(SalesPerson.class, salesPersonId);
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
	public List<SalesPerson> salesPersonList(long adminId) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		
		Criteria c = session.createCriteria(SalesPerson.class);
		c.createAlias("admin", "a");
		c.add(Restrictions.eq("a.id", adminId));
		
		List<SalesPerson> salesPersonList = c.list();
		tx.commit();
		session.close();
		return salesPersonList;
	}
	
	@Override
	public SalesPerson getSalesPersonById(long id) {
		Session session;
		SalesPerson  salesPerson = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(SalesPerson.class);
			 criteria.add(Restrictions.eq("id", id));
			 Object result=criteria.uniqueResult();
			 salesPerson = (SalesPerson)result;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return salesPerson;
	}
	
	@Override
	public boolean login(AdminDTO adminDTO) {
		boolean flag=true;
	    try{  	
	    session = sessionFactory.openSession();
		Criteria c = session.createCriteria(SalesPerson.class);
		c.add(Restrictions.eq("username",adminDTO.getUsername()));
		c.add(Restrictions.eq("password", adminDTO.getPassword()));
		Object o = c.uniqueResult();
		if(o==null)
		{
			flag=false;
		}
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return flag;	
	}
	
	@Override
	public SalesPerson getSalesPersonByUsername(String username) {
		Session session;
		SalesPerson  salesPerson = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(SalesPerson.class);
			 criteria.add(Restrictions.eq("username", username));
			 Object result=criteria.uniqueResult();
			 salesPerson = (SalesPerson)result;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return salesPerson;
	}
}
