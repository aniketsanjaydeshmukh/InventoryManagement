package com.inventory.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.DTO.AdminDTO;
import com.inventory.dao.CheckerDao;
import com.inventory.model.Checker;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
@Service("checkerServices")
public class CheckerServicesImpl implements CheckerServices{

	@Autowired
	CheckerDao checkerDao;

	@Override
	public boolean addOrUpdateChecker(Checker checker) {
		return checkerDao.addOrUpdateChecker(checker);
	}

	@Override
	public boolean checkUnique(String username, String email) {
		return checkerDao.checkUnique(username, email);
	}

	@Override
	public boolean deleteChecker(long checkerId) {
		return checkerDao.deleteChecker(checkerId);
	}

	@Override
	public List<Checker> checkerList(long adminId) {
		return checkerDao.checkerList(adminId); 
	}

	@Override
	public Checker getCheckerById(long id) {
		return checkerDao.getCheckerById(id);
	}

	@Override
	public boolean login(AdminDTO adminDTO) {
		return checkerDao.login(adminDTO);
	}

	@Override
	public Checker getCheckerByUsername(String username) {
		return checkerDao.getCheckerByUsername(username);
	}
	
}
