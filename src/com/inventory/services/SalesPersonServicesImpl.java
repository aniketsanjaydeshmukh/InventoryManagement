package com.inventory.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.DTO.AdminDTO;
import com.inventory.dao.SalesPersonDao;
import com.inventory.model.SalesPerson;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
@Service("salesPersonServices")
public class SalesPersonServicesImpl implements SalesPersonServices{

	@Autowired
	SalesPersonDao salesPersonDao;
	
	@Override
	public boolean addOrUpdateSalesPerson(SalesPerson salesPerson) {
		return salesPersonDao.addOrUpdateSalesPerson(salesPerson);
	}

	@Override
	public boolean checkUnique(String username, String email) {
		return salesPersonDao.checkUnique(username, email);
	}

	@Override
	public boolean deleteSalesPerson(long checkerId) {
		return salesPersonDao.deleteSalesPerson(checkerId);
	}

	@Override
	public List<SalesPerson> salesPersonList(long adminId) {
		return salesPersonDao.salesPersonList(adminId);
	}

	@Override
	public SalesPerson getSalesPersonById(long id) {
		return salesPersonDao.getSalesPersonById(id);
	}

	@Override
	public boolean login(AdminDTO adminDTO) {
		return salesPersonDao.login(adminDTO);
	}

	@Override
	public SalesPerson getSalesPersonByUsername(String username) {
		return salesPersonDao.getSalesPersonByUsername(username);
	}

}
