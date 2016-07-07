package com.inventory.services;

import java.util.List;

import com.inventory.DTO.AdminDTO;
import com.inventory.model.Maker;

public interface MakerServices {
	boolean addOrUpdateMaker(Maker maker);
	boolean checkUnique(String username,String email);
	boolean deleteMaker(long checkerId);
	List<Maker> makerList(long adminId);
	Maker getMakerById(long id);
	boolean login(AdminDTO adminDTO);
	Maker getMakerByUsername(String username);
}
