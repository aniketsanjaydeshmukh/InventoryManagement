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
import com.inventory.model.Admin;
import com.inventory.model.Checker;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
public class CheckerDaoImpl implements CheckerDao{
	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;


	public boolean addOrUpdateChecker(Checker checker) {
		boolean flag = false;
	    try{    
	    	session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(checker);
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
			Criteria c = session.createCriteria(Checker.class);
			
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
	public boolean deleteChecker(long checkerId) {
		boolean flag = true;
		try{
		session = sessionFactory.openSession();
		Object o = session.load(Checker.class, checkerId);
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
	public List<Checker> checkerList(long adminId) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		
		Criteria c = session.createCriteria(Checker.class);
		c.createAlias("admin", "a");
		c.add(Restrictions.eq("a.id", adminId));
		
		List<Checker> checkerList = c.list();
		tx.commit();
		session.close();
		return checkerList;
	}


	@Override
	public Checker getCheckerById(long id) {
		Session session;
		Checker  checker = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Checker.class);
			 criteria.add(Restrictions.eq("id", id));
			 Object result=criteria.uniqueResult();
			 checker = (Checker)result;
			 session.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return checker;
	}


	@Override
	public boolean login(AdminDTO adminDTO) {
		  boolean flag=true;
		    try{  	
		    session = sessionFactory.openSession();
			Criteria c = session.createCriteria(Checker.class);
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
	public Checker getCheckerByUsername(String username) {
		Session session;
		Checker  checker = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Checker.class);
			 criteria.add(Restrictions.eq("username", username));
			 Object result=criteria.uniqueResult();
			 checker = (Checker)result;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return checker;
	}
}
