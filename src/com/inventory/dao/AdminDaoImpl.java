package com.inventory.dao;

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

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
public class AdminDaoImpl implements AdminDao{
	
	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;
	
	@Override
	public boolean addOrUpdateAdmin(AdminDTO adminDTO){
		boolean flag = false;
		Admin admin = new Admin(adminDTO);
	    try{    
	    	session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(admin);
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
			Criteria c = session.createCriteria(Admin.class);
			
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
	public boolean login(AdminDTO adminDTO) {
		  boolean flag=true;
		    try{  	
		    session = sessionFactory.openSession();
			Criteria c = session.createCriteria(Admin.class);
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
	public Admin getAdminByUsername(String username) {
		Session session;
		Admin  admin = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Admin.class);
			 criteria.add(Restrictions.eq("username", username));
			 Object result=criteria.uniqueResult();
			 admin = (Admin)result;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return admin;
	}

	@Override
	public Admin getAdminById(long id) {
		Session session;
		Admin  admin = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Admin.class);
			 criteria.add(Restrictions.eq("id", id));
			 Object result=criteria.uniqueResult();
			 admin = (Admin)result;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return admin;
	}
}
