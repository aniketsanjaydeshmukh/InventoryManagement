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

import com.inventory.model.Checker;
import com.inventory.model.Product;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
public class ProductDaoImpl implements ProductDao{
	
	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;

	@Override
	public boolean addOrUpdateProduct(Product product) {
		boolean flag = false;
		try{    
	    	session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(product);
			tx.commit();
			session.close();
			flag = true;
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return flag;	
	}

	@Override
	public Product getProductByNameAndModel(String productName,
			String modelNumber) {
		Session session;
		Product  product = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Product.class);
			 criteria.add(Restrictions.eq("productName", productName));
			 criteria.add(Restrictions.eq("modelNumber", modelNumber));
			 Object result=criteria.uniqueResult();
			 product = (Product)result;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return product;
	}

	@Override
	public boolean deleteProduct(long productId) {
		boolean flag = true;
		try{
		session = sessionFactory.openSession();
		Object o = session.load(Product.class, productId);
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
	public List<Product> productList() {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		
		Criteria c = session.createCriteria(Product.class);

		List<Product> productList = c.list();
		tx.commit();
		session.close();
		return productList;
	}

	@Override
	public Product getProductById(long productId) {
		Session session;
		Product  product = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Product.class);
			 criteria.add(Restrictions.eq("id", productId));
			 Object result=criteria.uniqueResult();
			 product = (Product)result;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return product;
	}
	
}
