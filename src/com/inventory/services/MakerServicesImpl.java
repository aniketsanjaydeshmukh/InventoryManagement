package com.inventory.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.DTO.AdminDTO;
import com.inventory.dao.MakerDao;
import com.inventory.model.Maker;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
@Service("makerServices")
public class MakerServicesImpl implements MakerServices{

	@Autowired
	MakerDao makerDao;
	
	@Override
	public boolean addOrUpdateMaker(Maker maker) {
		return makerDao.addOrUpdateMaker(maker);
	}

	@Override
	public boolean checkUnique(String username, String email) {
		return makerDao.checkUnique(username, email);
	}

	@Override
	public boolean deleteMaker(long checkerId) {
		return makerDao.deleteMaker(checkerId);
	}

	@Override
	public List<Maker> makerList(long adminId) {
		return makerDao.makerList(adminId);
	}

	@Override
	public Maker getMakerById(long id) {
		return makerDao.getMakerById(id);
	}

	@Override
	public boolean login(AdminDTO adminDTO) {
		return makerDao.login(adminDTO);
	}

	@Override
	public Maker getMakerByUsername(String username) {
		return makerDao.getMakerByUsername(username);
	}

}
