package com.inventory.services;

import com.inventory.DTO.AdminDTO;
import com.inventory.model.Admin;

public interface AdminServices {
	boolean addOrUpdateAdmin(AdminDTO adminDTO);
	boolean checkUnique(String username,String email);
	boolean login(AdminDTO adminDTO);
	Admin getAdminByUsername(String username);
	Admin getAdminById(long id);
}
