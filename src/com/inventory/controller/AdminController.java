package com.inventory.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inventory.DTO.AdminDTO;
import com.inventory.model.Admin;
import com.inventory.model.Checker;
import com.inventory.model.Customer;
import com.inventory.model.Maker;
import com.inventory.model.SalesPerson;
import com.inventory.model.Supplier;
import com.inventory.services.AdminServices;
import com.inventory.services.CheckerServices;
import com.inventory.services.CustomerServices;
import com.inventory.services.MakerServices;
import com.inventory.services.SalesPersonServices;
import com.inventory.services.SupplierServices;

import flexjson.JSONSerializer;

@Controller
@RequestMapping("/admin")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminController {

	@Autowired
	AdminServices adminServices;
	
	@Autowired
	CheckerServices checkerServices;
	
	@Autowired
	MakerServices makerServices;
	
	@Autowired
	SupplierServices supplierServices;
	
	@Autowired
	CustomerServices customerServices;
	
	@Autowired
	SalesPersonServices salesPersonServices;

	public static HashMap<String, String> userMap = new HashMap<String, String>();
	
	@RequestMapping(value = "/createAdmin/",method = RequestMethod.POST,  headers = "content-type=application/json")
	public @ResponseBody
	void add(@RequestBody AdminDTO adminDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String,Object> obj = new HashMap<String,Object>();
		 @SuppressWarnings("unused")
		String sessionId = null;
			if(!adminServices.checkUnique(adminDTO.getUsername(),adminDTO.getEmail()))
			{
			   obj.put("admin", "fail");
			   obj.put("reason", "username/email must be  Unique");
			}			
			else
			{ if(adminServices.addOrUpdateAdmin(adminDTO));
		
				{
					obj.put("admin", "added");
				}	
			}
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
	}
	
	@RequestMapping(value = "/login/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void login(@RequestBody AdminDTO user,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String,Object> obj = new HashMap<String,Object>();
		 String sessionId = null;
			if(adminServices.login(user))
			{	
				 HttpSession sessionn = request.getSession();
				 sessionId = sessionn.getId();
				 userMap.put(sessionId, sessionId);
				 Admin admin1 = adminServices.getAdminByUsername(user.getUsername());
				 String strI = Long.toString((admin1.getId()));
				 String adminId = "adminId"+sessionId;
				 userMap.put(adminId,strI);
				 obj.put("sessionId", sessionId);
				 obj.put("login", "successful");
			}
			else
			{
				obj.put("login", "unsuccessful");
			}
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));		
	}

	@RequestMapping(value = "/logout/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void logout(@RequestBody AdminDTO adminDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(adminDTO.getSessionId());
		Map<String,Object> obj = new HashMap<String,Object>();
		if(key != null){
				user.remove(adminDTO.getSessionId());
				user.remove("adminId"+adminDTO.getSessionId());
				obj.put("logout", "successful");
		}else{
			obj.put("logout", "unsuccessful");
		}
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));	
	}

	public HashMap<String, String> getUsermap() {    
	    return userMap;
	}
	
	@RequestMapping(value = "/addChecker/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void addChecker(@RequestBody AdminDTO checkerDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(checkerDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("adminId"+checkerDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			Admin admin = adminServices.getAdminById(adminId);
			Checker checker = new Checker(checkerDTO);
			checker.setAdmin(admin);
			Map<String,Object> obj = new HashMap<String,Object>();
		
				if((!checkerServices.checkUnique(checkerDTO.getUsername(),checkerDTO.getEmail())) && (!adminServices.checkUnique(checkerDTO.getUsername(),checkerDTO.getEmail())) && (!makerServices.checkUnique(checkerDTO.getUsername(),checkerDTO.getEmail())))
				{
				   obj.put("checker", "fail");
				   obj.put("reason", "username/email must be  Unique");
				}			
				else
				{ if(checkerServices.addOrUpdateChecker(checker));
			
					{
						obj.put("checker", "added");
					}	
				}
				response.setContentType("application/json; charset=UTF-8"); 
				response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
			}
		}
	
	// Admin can delete Checker Account
	@RequestMapping(value = "/deleteChecker/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void deleteChecker(@RequestBody AdminDTO checkerDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(checkerDTO.getSessionId());
		if(key != null){
	
			Map<String,Object> obj = new HashMap<String,Object>();
			
			if(checkerServices.deleteChecker(checkerDTO.getId())){
				obj.put("checker", "deleted");
			}else{
				obj.put("checkerDeleted", "fail");
				obj.put("reason", "not present");
			}
			
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
		}
	}
	
	// list of checker
	@RequestMapping(value = "/listChecker/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void listChecker(@RequestBody AdminDTO checkerDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(checkerDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("adminId"+checkerDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			List<Checker> checkerList = checkerServices.checkerList(adminId);
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			
			for (Checker checker : checkerList) {
				Map<String,Object> obj = new HashMap<String,Object>();
				obj.put("id", checker.getId());
				obj.put("address", checker.getAddress());
				obj.put("email", checker.getEmail());
				obj.put("name", checker.getName());
				obj.put("password", checker.getPassword());
				obj.put("userRole", checker.getUserRole());
				obj.put("username", checker.getUsername());
				list.add(obj);
			}
		
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(list));
		}
	}
	
	//update in checker by Admin
	// id and userRole  will not be editable 
	@RequestMapping(value = "/updateChecker/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void updateChecker(@RequestBody AdminDTO checkerDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(checkerDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("adminId"+checkerDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			Checker checker = new Checker(checkerDTO);
			checker.setId(checkerDTO.getId());
			Admin admin = adminServices.getAdminById(adminId);
			checker.setAdmin(admin);
			
			Map<String,Object> obj = new HashMap<String,Object>();
			
			Checker oldChecker = checkerServices.getCheckerById(checkerDTO.getId());
			
			if((oldChecker.getEmail().equals(checkerDTO.getEmail())) &&(oldChecker.getUsername().equals(checkerDTO.getUsername()))){
				if((!adminServices.checkUnique(checkerDTO.getUsername(),checkerDTO.getEmail())) && (!salesPersonServices.checkUnique(checkerDTO.getUsername(),checkerDTO.getEmail())) && (!makerServices.checkUnique(checkerDTO.getUsername(),checkerDTO.getEmail())))
				{
				   obj.put("checkerUpdated", "fail");
				   obj.put("reason", "username/email must be  Unique");
				}			
				else
				{ if(checkerServices.addOrUpdateChecker(checker));
			
					{
						obj.put("checker", "updated");
					}	
				}
			}else{
				if((!adminServices.checkUnique(checkerDTO.getUsername(),checkerDTO.getEmail())) && (!salesPersonServices.checkUnique(checkerDTO.getUsername(),checkerDTO.getEmail())) && (!checkerServices.checkUnique(checkerDTO.getUsername(),checkerDTO.getEmail())) && (!makerServices.checkUnique(checkerDTO.getUsername(),checkerDTO.getEmail())))
				{
				   obj.put("checkerUpdated", "fail");
				   obj.put("reason", "username/email must be  Unique");
				}			
				else
				{ if(checkerServices.addOrUpdateChecker(checker));
			
					{
						obj.put("checker", "updated");
					}	
				}
			}
			
			
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
			
		}
	}
	
	
	@RequestMapping(value = "/addMaker/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void addMaker(@RequestBody AdminDTO makerDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		System.out.println(makerDTO.getSessionId());
		Object key = user.get(makerDTO.getSessionId());
		
		if(key != null){
			System.out.println("key not null");
			Object keyId = user.get("adminId"+makerDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			System.out.println("adminID:"+adminId);
			Admin admin = adminServices.getAdminById(adminId);
			Maker maker = new Maker(makerDTO);
			maker.setAdmin(admin);
			Map<String,Object> obj = new HashMap<String,Object>();
		
				if((!makerServices.checkUnique(makerDTO.getUsername(),makerDTO.getEmail())) && (!adminServices.checkUnique(makerDTO.getUsername(),makerDTO.getEmail())) && (!checkerServices.checkUnique(makerDTO.getUsername(),makerDTO.getEmail())))
				{
				   obj.put("maker", "fail");
				   obj.put("reason", "username/email must be  Unique");
				}			
				else
				{ if(makerServices.addOrUpdateMaker(maker));
			
					{
						obj.put("maker", "added");
					}	
				}
				response.setContentType("application/json; charset=UTF-8"); 
				response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
			}
		}
	
	// Admin can delete Maker Account
	@RequestMapping(value = "/deleteMaker/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void deleteMaker(@RequestBody AdminDTO makerDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(makerDTO.getSessionId());
		if(key != null){
	
			Map<String,Object> obj = new HashMap<String,Object>();
			
			if(makerServices.deleteMaker(makerDTO.getId())){
				obj.put("maker", "deleted");
			}else{
				obj.put("makerDeleted", "fail");
				obj.put("reason", "not present");
			}
			
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
		}
	}
	
	// list of maker
	@RequestMapping(value = "/listMaker/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void listMaker(@RequestBody AdminDTO makerDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(makerDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("adminId"+makerDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			List<Maker> makerList = makerServices.makerList(adminId);
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			
			for (Maker maker : makerList) {
				Map<String,Object> obj = new HashMap<String,Object>();
				obj.put("id", maker.getId());
				obj.put("address", maker.getAddress());
				obj.put("email", maker.getEmail());
				obj.put("name", maker.getName());
				obj.put("password", maker.getPassword());
				obj.put("userRole", maker.getUserRole());
				obj.put("username", maker.getUsername());
				list.add(obj);
			}
		
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(list));
		}
	}
	
	//update in maker by Admin
	// id and userRole  will not be editable 
	@RequestMapping(value = "/updateMaker/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void updateMaker(@RequestBody AdminDTO makerDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(makerDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("adminId"+makerDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			Maker maker = new Maker(makerDTO);
			maker.setId(makerDTO.getId());
			Admin admin = adminServices.getAdminById(adminId);
			maker.setAdmin(admin);
			
			Map<String,Object> obj = new HashMap<String,Object>();
			
			Maker oldMaker = makerServices.getMakerById(makerDTO.getId());
			
			if((oldMaker.getEmail().equals(makerDTO.getEmail())) &&(oldMaker.getUsername().equals(makerDTO.getUsername()))){
				if((!adminServices.checkUnique(makerDTO.getUsername(),makerDTO.getEmail())) && (!salesPersonServices.checkUnique(makerDTO.getUsername(),makerDTO.getEmail())) && (!checkerServices.checkUnique(makerDTO.getUsername(),makerDTO.getEmail())))
				{
				   obj.put("makerUpdated", "fail");
				   obj.put("reason", "username/email must be  Unique");
				}			
				else
				{ if(makerServices.addOrUpdateMaker(maker));
			
					{
						obj.put("maker", "updated");
					}	
				}
			}else{
				if((!adminServices.checkUnique(makerDTO.getUsername(),makerDTO.getEmail())) && (!salesPersonServices.checkUnique(makerDTO.getUsername(),makerDTO.getEmail())) && (!checkerServices.checkUnique(makerDTO.getUsername(),makerDTO.getEmail())) && (!makerServices.checkUnique(makerDTO.getUsername(),makerDTO.getEmail())))
				{
				   obj.put("makerUpdated", "fail");
				   obj.put("reason", "username/email must be  Unique");
				}			
				else
				{ if(makerServices.addOrUpdateMaker(maker));
			
					{
						obj.put("maker", "updated");
					}	
				}
			}
				
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
			
		}
	}
	
	@RequestMapping(value = "/addSupplier/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void addSupplier(@RequestBody AdminDTO supplierDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(supplierDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("adminId"+supplierDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			Admin admin = adminServices.getAdminById(adminId);
			Supplier supplier = new Supplier(supplierDTO);
			supplier.setAdmin(admin);
			Map<String,Object> obj = new HashMap<String,Object>();
			
			if(supplierServices.addOrUpdateSupplier(supplier)){
				obj.put("supplier", "added");
			}
			
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
		}
	}
	
	@RequestMapping(value = "/deleteSupplier/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void deleteSupplier(@RequestBody AdminDTO supplierDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(supplierDTO.getSessionId());
		if(key != null){
	
			Map<String,Object> obj = new HashMap<String,Object>();
			
			if(supplierServices.deleteSupplier(supplierDTO.getId())){
				obj.put("supplier", "deleted");
			}else{
				obj.put("supplierDeleted", "fail");
				obj.put("reason", "not present");
			}
			
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
		}
	}
	
	@RequestMapping(value = "/updateSupplier/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void updateSupplier(@RequestBody AdminDTO supplierDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(supplierDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("adminId"+supplierDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			Supplier supplier = new Supplier(supplierDTO);
			supplier.setId(supplierDTO.getId());
			Admin admin = adminServices.getAdminById(adminId);
			supplier.setAdmin(admin);
			
			Map<String,Object> obj = new HashMap<String,Object>();
			
			if(supplierServices.addOrUpdateSupplier(supplier)){
				obj.put("supplier", "updated");
			}
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
		}
	}
	
	@RequestMapping(value = "/listSupplier/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void listSupplier(@RequestBody AdminDTO supplierDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(supplierDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("adminId"+supplierDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			List<Supplier> supplierList = supplierServices.supplierList(adminId);
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			
			for (Supplier supplier : supplierList) {
				Map<String,Object> obj = new HashMap<String,Object>();
				obj.put("id", supplier.getId());
				obj.put("address", supplier.getAddress());
				obj.put("email", supplier.getEmail());
				obj.put("name", supplier.getName());
				obj.put("contactNo", supplier.getContactNo());
				list.add(obj);
			}
		
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(list));
		}
	}
	
	@RequestMapping(value = "/addCustomer/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void addCustomer(@RequestBody AdminDTO customerDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(customerDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("adminId"+customerDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			Admin admin = adminServices.getAdminById(adminId);
			Customer customer = new Customer(customerDTO);
			customer.setAdmin(admin);
			Map<String,Object> obj = new HashMap<String,Object>();
			
			if(customerServices.addOrUpdateCustomer(customer)){
				obj.put("customer", "added");
			}
			
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
		}
	}
	
	@RequestMapping(value = "/deleteCustomer/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void deleteCustomer(@RequestBody AdminDTO customerDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(customerDTO.getSessionId());
		if(key != null){
	
			Map<String,Object> obj = new HashMap<String,Object>();
			
			if(customerServices.deleteCustomer(customerDTO.getId())){
				obj.put("customer", "deleted");
			}else{
				obj.put("customerDeleted", "fail");
				obj.put("reason", "not present");
			}
			
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
		}
	}
	
	@RequestMapping(value = "/updateCustomer/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void updateCustomer(@RequestBody AdminDTO customerDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(customerDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("adminId"+customerDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			Customer customer = new Customer(customerDTO);
			customer.setId(customerDTO.getId());
			Admin admin = adminServices.getAdminById(adminId);
			customer.setAdmin(admin);
			
			Map<String,Object> obj = new HashMap<String,Object>();
			
			if(customerServices.addOrUpdateCustomer(customer)){
				obj.put("customer", "updated");
			}
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
		}
	}
	
	@RequestMapping(value = "/listCustomer/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void listCustomer(@RequestBody AdminDTO customerDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(customerDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("adminId"+customerDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			List<Customer> customerList = customerServices.customerList(adminId);
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			
			for (Customer customer : customerList) {
				Map<String,Object> obj = new HashMap<String,Object>();
				obj.put("id", customer.getId());
				obj.put("address", customer.getAddress());
				obj.put("email", customer.getEmail());
				obj.put("name", customer.getName());
				obj.put("contactNo", customer.getContactNo());
				list.add(obj);
			}
		
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(list));
		}
	}
	
	@RequestMapping(value = "/addSalesPerson/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void addSalesPerson(@RequestBody AdminDTO checkerDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(checkerDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("adminId"+checkerDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			Admin admin = adminServices.getAdminById(adminId);
			
			if(admin != null){
				SalesPerson salesPerson = new SalesPerson(checkerDTO);
				salesPerson.setAdmin(admin);
				
				Map<String,Object> obj = new HashMap<String,Object>();
		
				if((!salesPersonServices.checkUnique(checkerDTO.getUsername(),checkerDTO.getEmail())) && (!adminServices.checkUnique(checkerDTO.getUsername(),checkerDTO.getEmail())) && (!checkerServices.checkUnique(checkerDTO.getUsername(),checkerDTO.getEmail())) && (!makerServices.checkUnique(checkerDTO.getUsername(),checkerDTO.getEmail())))
				{
				   obj.put("salesPerson", "fail");
				   obj.put("reason", "username/email must be  Unique");
				}			
				else
				{ if(salesPersonServices.addOrUpdateSalesPerson(salesPerson));
			
					{
						obj.put("salesPerson", "added");
					}	
				}
				response.setContentType("application/json; charset=UTF-8"); 
				response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
			}
		}
	}
	
	@RequestMapping(value = "/deleteSalesPerson/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void deleteSalesPerson(@RequestBody AdminDTO salesPersonDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(salesPersonDTO.getSessionId());
		if(key != null){
	
			Object keyId = user.get("adminId"+salesPersonDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			Admin admin = adminServices.getAdminById(adminId);
			
			if(admin != null){
			
			
				Map<String,Object> obj = new HashMap<String,Object>();
			
				if(salesPersonServices.deleteSalesPerson(salesPersonDTO.getId())){
					obj.put("salesPerson", "deleted");
				}else{
					obj.put("salesPersonDeleted", "fail");
					obj.put("reason", "not present");
				}
			
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
			
			}
		}
	}

	@RequestMapping(value = "/listSalesPerson/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void listSalesPerson(@RequestBody AdminDTO makerDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(makerDTO.getSessionId());
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		if(key != null){
			Object keyId = user.get("adminId"+makerDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			Admin admin = adminServices.getAdminById(adminId);
			
			if(admin != null){
				List<SalesPerson> salesPersonList = salesPersonServices.salesPersonList(adminId);
				
				for (SalesPerson salesPerson : salesPersonList) {
					Map<String,Object> obj = new HashMap<String,Object>();
					obj.put("id", salesPerson.getId());
					obj.put("address", salesPerson.getAddress());
					obj.put("email", salesPerson.getEmail());
					obj.put("name", salesPerson.getName());
					obj.put("password", salesPerson.getPassword());
					obj.put("userRole", salesPerson.getUserRole());
					obj.put("username", salesPerson.getUsername());
					list.add(obj);
				}
			}
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(list));
		}
	}
	
	@RequestMapping(value = "/updateSalesPerson/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void updateSalesPerson(@RequestBody AdminDTO salesPersonDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		AdminController ac = new AdminController();
		HashMap<String, String> user = ac.getUsermap();
		Object key = user.get(salesPersonDTO.getSessionId());
	
		if(key != null){
		
			Object keyId = user.get("adminId"+salesPersonDTO.getSessionId());
			long adminId = Long.parseLong(keyId.toString());
			Admin admin = adminServices.getAdminById(adminId);
			
			if(admin != null){
					SalesPerson salesPerson = new SalesPerson(salesPersonDTO);
					salesPerson.setId(salesPersonDTO.getId());
					salesPerson.setAdmin(admin);
					
					Map<String,Object> obj = new HashMap<String,Object>();
					
					SalesPerson oldSalesPerson = salesPersonServices.getSalesPersonById(salesPersonDTO.getId());
					
					if((oldSalesPerson.getEmail().equals(salesPersonDTO.getEmail())) &&(oldSalesPerson.getUsername().equals(salesPersonDTO.getUsername()))){
						if((!adminServices.checkUnique(salesPersonDTO.getUsername(),salesPersonDTO.getEmail())) && (!makerServices.checkUnique(salesPersonDTO.getUsername(),salesPersonDTO.getEmail())  && (!checkerServices.checkUnique(salesPersonDTO.getUsername(),salesPersonDTO.getEmail()))))
						{
						   obj.put("salesPersonUpdated", "fail");
						   obj.put("reason", "username/email must be  Unique");
						}			
						else
						{ if(salesPersonServices.addOrUpdateSalesPerson(salesPerson));
					
							{
								obj.put("salesPerson", "updated");
							}	
						}
					}else{
						if((!adminServices.checkUnique(salesPersonDTO.getUsername(),salesPersonDTO.getEmail())) && (!salesPersonServices.checkUnique(salesPersonDTO.getUsername(),salesPersonDTO.getEmail())) && (!checkerServices.checkUnique(salesPersonDTO.getUsername(),salesPersonDTO.getEmail())) && (!makerServices.checkUnique(salesPersonDTO.getUsername(),salesPersonDTO.getEmail())))
						{
						   obj.put("salesPersonUpdated", "fail");
						   obj.put("reason", "username/email must be  Unique");
						}			
						else
						{ if(salesPersonServices.addOrUpdateSalesPerson(salesPerson));
					
							{
								obj.put("salesPerson", "updated");
							}	
						}
					}
						
					response.setContentType("application/json; charset=UTF-8"); 
					response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
					
		}
	}
	}
}
