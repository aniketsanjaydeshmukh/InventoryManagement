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

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
public class MakerDaoImpl implements MakerDao{
	
	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;

	@Override
	public boolean addOrUpdateMaker(Maker maker) {
		boolean flag = false;
	    try{    
	    	session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(maker);
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
			Criteria c = session.createCriteria(Maker.class);
			
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
	public boolean deleteMaker(long checkerId) {
		boolean flag = true;
		try{
		session = sessionFactory.openSession();
		Object o = session.load(Maker.class, checkerId);
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
	public List<Maker> makerList(long adminId) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		
		Criteria c = session.createCriteria(Maker.class);
		c.createAlias("admin", "a");
		c.add(Restrictions.eq("a.id", adminId));
		
		List<Maker> makerList = c.list();
		tx.commit();
		session.close();
		return makerList;
	}

	@Override
	public Maker getMakerById(long id) {
		Session session;
		Maker  maker = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Maker.class);
			 criteria.add(Restrictions.eq("id", id));
			 Object result=criteria.uniqueResult();
			 maker = (Maker)result;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return maker;
	}

	@Override
	public boolean login(AdminDTO adminDTO) {
		  boolean flag=true;
		    try{  	
		    session = sessionFactory.openSession();
			Criteria c = session.createCriteria(Maker.class);
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
	public Maker getMakerByUsername(String username) {
		Session session;
		Maker  maker = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Maker.class);
			 criteria.add(Restrictions.eq("username", username));
			 Object result=criteria.uniqueResult();
			 maker = (Maker)result;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return maker;
	}
}
