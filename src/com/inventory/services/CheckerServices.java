package com.inventory.services;

import java.util.List;

import com.inventory.DTO.AdminDTO;
import com.inventory.model.Checker;

public interface CheckerServices {
	boolean addOrUpdateChecker(Checker checker);
	boolean checkUnique(String username,String email);
	boolean deleteChecker(long checkerId);
	List<Checker> checkerList(long adminId);
	Checker getCheckerById(long id);
	boolean login(AdminDTO adminDTO);
	Checker getCheckerByUsername(String username);
}
