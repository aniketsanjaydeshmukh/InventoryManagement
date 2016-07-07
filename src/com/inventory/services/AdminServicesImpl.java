package com.inventory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.DTO.AdminDTO;
import com.inventory.dao.AdminDao;
import com.inventory.model.Admin;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
@Service("adminServices")
public class AdminServicesImpl implements AdminServices{

	@Autowired
	AdminDao adminDao;

	@Override
	public boolean addOrUpdateAdmin(AdminDTO adminDTO) {
		return adminDao.addOrUpdateAdmin(adminDTO);
	}

	@Override
	public boolean checkUnique(String username, String email) {
		return adminDao.checkUnique(username, email);
	}

	@Override
	public boolean login(AdminDTO adminDTO) {
		return adminDao.login(adminDTO);
	}

	@Override
	public Admin getAdminByUsername(String username) {
		return adminDao.getAdminByUsername(username);
	}

	@Override
	public Admin getAdminById(long id) {
		return adminDao.getAdminById(id);
	}
	
}
