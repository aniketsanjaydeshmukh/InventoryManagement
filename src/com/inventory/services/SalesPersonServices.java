package com.inventory.services;

import java.util.List;

import com.inventory.DTO.AdminDTO;
import com.inventory.model.SalesPerson;

public interface SalesPersonServices {
	boolean addOrUpdateSalesPerson(SalesPerson salesPerson);
	boolean checkUnique(String username,String email);
	boolean deleteSalesPerson(long checkerId);
	List<SalesPerson> salesPersonList(long adminId);
	SalesPerson getSalesPersonById(long id);
	boolean login(AdminDTO adminDTO);
	SalesPerson getSalesPersonByUsername(String username);
}
