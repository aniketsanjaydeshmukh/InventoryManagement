package com.inventory.dao;

import com.inventory.DTO.AdminDTO;
import com.inventory.model.Admin;

public interface AdminDao {
	boolean addOrUpdateAdmin(AdminDTO adminDTO);
	boolean checkUnique(String username,String email);
	boolean login(AdminDTO adminDTO);
	Admin getAdminByUsername(String username);
	Admin getAdminById(long id);
}
