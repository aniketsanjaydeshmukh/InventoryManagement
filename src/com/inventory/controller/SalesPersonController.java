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
import com.inventory.DTO.SaleInvoiceDTO;
import com.inventory.model.Admin;
import com.inventory.model.Checker;
import com.inventory.model.Customer;
import com.inventory.model.CustomerReport;
import com.inventory.model.Maker;
import com.inventory.model.Product;
import com.inventory.model.SaleInvoice;
import com.inventory.model.SalesPerson;
import com.inventory.model.StockReport;
import com.inventory.model.TaxInvoice;
import com.inventory.services.CustomerReportServices;
import com.inventory.services.CustomerServices;
import com.inventory.services.ProductServices;
import com.inventory.services.SaleInvoiceServices;
import com.inventory.services.SalesPersonServices;
import com.inventory.services.StockReportServices;
import com.inventory.services.TaxInvoiceServices;

import flexjson.JSONSerializer;

@Controller
@RequestMapping("/salesPerson")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesPersonController {

	@Autowired
	SalesPersonServices salesPersonServices;
	
	@Autowired
	StockReportServices stockReportServices;
	
	@Autowired
	ProductServices productServices;
	
	@Autowired
	CustomerReportServices customerReportServices;
	
	@Autowired
	TaxInvoiceServices taxInvoiceServices;
	
	@Autowired
	CustomerServices customerServices;
	
	@Autowired
	SaleInvoiceServices saleInvoiceServices;
	
	public static HashMap<String, String> salesPersonMap = new HashMap<String, String>();
	
	@RequestMapping(value = "/login/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void login(@RequestBody AdminDTO user,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String,Object> obj = new HashMap<String,Object>();
		 String sessionId = null;
			if(salesPersonServices.login(user))
			{	
				 HttpSession sessionn = request.getSession();
				 sessionId = sessionn.getId();
				 salesPersonMap.put(sessionId, sessionId);
				 SalesPerson salesPerson = salesPersonServices.getSalesPersonByUsername(user.getUsername());
				 String strI = Long.toString((salesPerson.getId()));
				 String makerId = "salesPersonId"+sessionId;
				 salesPersonMap.put(makerId,strI);
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
	
	@RequestMapping(value = "/getStockByProduct/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void getStockByProduct(@RequestBody AdminDTO adminDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
	
		SalesPersonController spc = new SalesPersonController();
		HashMap<String, String> user = spc.getSalesPersonMap();
		Object key = user.get(adminDTO.getSessionId());
		Map<String,Object> obj = new HashMap<String, Object>();
		
		if(key != null){
			Object keyId = user.get("salesPersonId"+adminDTO.getSessionId());
			long salesPersonId = Long.parseLong(keyId.toString());
			SalesPerson salesPerson = salesPersonServices.getSalesPersonById(salesPersonId);
			
			if(salesPerson != null){
				StockReport st = stockReportServices.getStockReportByProductId(adminDTO.getProductId());
				
				obj.put("stockReportId", st.getId());
				obj.put("productId", st.getProduct().getId());
				obj.put("productName", st.getProduct().getProductName());
				obj.put("modelNumber", st.getProduct().getModelNumber());
				obj.put("productType", st.getProduct().getProductType());
				obj.put("unitPrice", st.getProduct().getUnitPrice());
				obj.put("discountRate", st.getProduct().getDiscountRate());
		
				obj.put("quantity", st.getUnits());		
			}
		}
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));		
	}
	
	
	@RequestMapping(value = "/createSaleInvoice/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void createSaleInvoice(@RequestBody SaleInvoiceDTO saleInvoiceDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		SalesPersonController spc = new SalesPersonController();
		HashMap<String, String> user = spc.getSalesPersonMap();
		Object key = user.get(saleInvoiceDTO.getSessionId());
		Map<String,Object> obj = new HashMap<String, Object>();
		
		if(key != null){
			Object keyId = user.get("salesPersonId"+saleInvoiceDTO.getSessionId());
			long salesPersonId = Long.parseLong(keyId.toString());
			SalesPerson salesPerson = salesPersonServices.getSalesPersonById(salesPersonId);
			
			if(salesPerson != null){
				SaleInvoice saleInvoice = new SaleInvoice();
				saleInvoice.setInvoiceDate(saleInvoiceDTO.getSaleInvoiceDate());
				saleInvoice.setPaymentMode(saleInvoiceDTO.getPaymentMode());
				Product product = productServices.getProductById(saleInvoiceDTO.getProductId());
				if(product != null){
					saleInvoice.setProduct(product);
					Customer customer = customerServices.getCustomerById(saleInvoiceDTO.getCustomerId());
					if(customer != null){
						saleInvoice.setCustomer(customer);
						StockReport st = stockReportServices.getStockReportByProductId(saleInvoiceDTO.getProductId());
						
						//check if quantity is enough or not
						if(st.getUnits() >= saleInvoiceDTO.getQuantity()){
							//subtract units from stock
							long remainingUnits = st.getUnits() - saleInvoiceDTO.getQuantity();
							st.setUnits(remainingUnits);
							stockReportServices.createOrUpdateStockReport(st);
							
							saleInvoice.setQuantity(saleInvoiceDTO.getQuantity());
							
							double unitPrice = product.getUnitPrice();
							double discountRate = product.getDiscountRate();
							double discountedAmountForSingleUnit = (discountRate/100)*unitPrice;
							
							double discountAmount = discountedAmountForSingleUnit * saleInvoiceDTO.getQuantity();
							
							double finalAmount = (saleInvoiceDTO.getQuantity()*product.getUnitPrice())-discountAmount;
							
							saleInvoice.setDiscountedAmount(discountAmount);
							saleInvoice.setFinalAmount(finalAmount);
							saleInvoice.setSalesPerson(salesPerson);
							
							//add sale invoice
							saleInvoiceServices.addOrUpdateSaleInvoice(saleInvoice);
							
							//generate tax invoice
							TaxInvoice ti = new TaxInvoice();
							ti.setSaleInvoice(saleInvoice);
							taxInvoiceServices.addOrUpdateTaxInvoice(ti);
							
							//generate customer report
							CustomerReport cr = new CustomerReport();
							cr.setCustomer(customer);
							cr.setSaleInvoice(saleInvoice);
							customerReportServices.addOrUpdateCustomerReport(cr);
							
							
							obj.put("saleInvoice", "created");
							
							
						}else{
							obj.put("saleInvoice", "not created");
							obj.put("reason", "quantity not enough");
						}
						
					}else{
						obj.put("saleInvoice", "not created");
						obj.put("reason", "customer is not present");
					}
					
				}else{
					obj.put("saleInvoice", "not created");
					obj.put("reason", "product is not present");
				}
				
			}
		}
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
	}
	
	@RequestMapping(value = "/logout/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void logout(@RequestBody AdminDTO adminDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		MakerController mc = new MakerController();
		HashMap<String, String> user = mc.getMakerMap();
		Object key = user.get(adminDTO.getSessionId());
		Map<String,Object> obj = new HashMap<String,Object>();
		if(key != null){
				user.remove(adminDTO.getSessionId());
				user.remove("salesPersonId"+adminDTO.getSessionId());
				obj.put("logout", "successful");
		}else{
			obj.put("logout", "unsuccessful");
		}
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));	
	}
	
	public HashMap<String, String> getSalesPersonMap() {    
	    return salesPersonMap;
	}
}
