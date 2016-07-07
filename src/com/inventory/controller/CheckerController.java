package com.inventory.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.inventory.DTO.ProductDTO;
import com.inventory.DTO.PurchaseInvoiceDTO;
import com.inventory.DTO.SaleInvoiceDTO;
import com.inventory.model.Admin;
import com.inventory.model.Checker;
import com.inventory.model.Customer;
import com.inventory.model.CustomerReport;
import com.inventory.model.Product;
import com.inventory.model.PurchaseInvoice;
import com.inventory.model.SaleInvoice;
import com.inventory.model.SalesPerson;
import com.inventory.model.StockReport;
import com.inventory.model.Supplier;
import com.inventory.model.SupplierReport;
import com.inventory.model.TaxInvoice;
import com.inventory.services.CheckerServices;
import com.inventory.services.CustomerReportServices;
import com.inventory.services.CustomerServices;
import com.inventory.services.ProductServices;
import com.inventory.services.PurchaseInvoiceServices;
import com.inventory.services.SaleInvoiceServices;
import com.inventory.services.SalesPersonServices;
import com.inventory.services.StockReportServices;
import com.inventory.services.SupplierReportServices;
import com.inventory.services.SupplierServices;
import com.inventory.services.TaxInvoiceServices;

import flexjson.JSONSerializer;


@Controller
@RequestMapping("/checker")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckerController {
	
	@Autowired
	CheckerServices checkerServices;
	
	@Autowired
	ProductServices productServices;
	
	@Autowired
	PurchaseInvoiceServices purchaseInvoiceServices;
	
	@Autowired
	SupplierServices supplierServices;
	
	@Autowired
	SupplierReportServices supplierReportServices;
	
	@Autowired
	StockReportServices stockReportServices;
	
	@Autowired
	CustomerServices customerServices;
	
	@Autowired
	SaleInvoiceServices saleInvoiceServices;
	
	@Autowired
	SalesPersonServices salesPersonServices;
	
	@Autowired
	TaxInvoiceServices taxInvoiceServices;
	
	@Autowired
	CustomerReportServices customerReportServices;
	
	public static HashMap<String, String> checkerMap = new HashMap<String, String>();
	
	
	@RequestMapping(value = "/login/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void login(@RequestBody AdminDTO user,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String,Object> obj = new HashMap<String,Object>();
		 String sessionId = null;
			if(checkerServices.login(user))
			{	
				 HttpSession sessionn = request.getSession();
				 sessionId = sessionn.getId();
				 checkerMap.put(sessionId, sessionId);
				 Checker checker1 = checkerServices.getCheckerByUsername(user.getUsername());
				 String strI = Long.toString((checker1.getId()));
				 String checkerId = "checkerId"+sessionId;
				 checkerMap.put(checkerId,strI);
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
		CheckerController cc = new CheckerController();
		HashMap<String, String> user = cc.getCheckerMap();
		Object key = user.get(adminDTO.getSessionId());
		Map<String,Object> obj = new HashMap<String,Object>();
		if(key != null){
				user.remove(adminDTO.getSessionId());
				user.remove("checkerId"+adminDTO.getSessionId());
				obj.put("logout", "successful");
		}else{
			obj.put("logout", "unsuccessful");
		}
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));	
	}
	
	@RequestMapping(value = "/addProduct/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void addProduct(@RequestBody ProductDTO productDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		CheckerController cc = new CheckerController();
		HashMap<String, String> user = cc.getCheckerMap();
		Object key = user.get(productDTO.getSessionId());
		Map<String,Object> obj = new HashMap<String,Object>();
		if(key != null){
			Object keyId = user.get("checkerId"+productDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Checker checker = checkerServices.getCheckerById(checkerId);
			//check if user is checker or not
			if(checker != null){
			Product product = new Product(productDTO);
			
			Product oldProduct = productServices.getProductByNameAndModel(productDTO.getProductName(), productDTO.getModelNumber());
			
			if(oldProduct != null){
				obj.put("product", "not added");
				obj.put("reason", "already present");
			}else{
				productServices.addOrUpdateProduct(product);
				obj.put("product", "added");
			}
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
			}
		}
	}
	
	@RequestMapping(value = "/deleteProduct/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void deleteProduct(@RequestBody ProductDTO productDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		CheckerController cc = new CheckerController();
		HashMap<String, String> user = cc.getCheckerMap();
		Object key = user.get(productDTO.getSessionId());
		if(key != null){
	
			Object keyId = user.get("checkerId"+productDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Checker checker = checkerServices.getCheckerById(checkerId);
			if(checker != null){
			Map<String,Object> obj = new HashMap<String,Object>();
			
			if(productServices.deleteProduct(productDTO.getId())){
				obj.put("product", "deleted");
			}else{
				obj.put("productDeleted", "fail");
				obj.put("reason", "not present");
			}
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
			}
		}
	}
	
	@RequestMapping(value = "/updateProduct/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void updateProduct(@RequestBody ProductDTO productDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		CheckerController cc = new CheckerController();
		HashMap<String, String> user = cc.getCheckerMap();
		Object key = user.get(productDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("checkerId"+productDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Checker checker = checkerServices.getCheckerById(checkerId);
			if(checker != null){
			Product product = new Product(productDTO);
			product.setId(productDTO.getId());
			
			Map<String,Object> obj = new HashMap<String,Object>();
			
			if(productServices.addOrUpdateProduct(product)){
				obj.put("product", "updated");
			}
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
			}
		}
	}
	
	@RequestMapping(value = "/listProduct/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void listCustomer(@RequestBody ProductDTO productDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		CheckerController cc = new CheckerController();
		HashMap<String, String> user = cc.getCheckerMap();
		Object key = user.get(productDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("checkerId"+productDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Checker checker = checkerServices.getCheckerById(checkerId);
			if(checker != null){
			List<Product> productList = productServices.productList();
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			
			for (Product product : productList) {
				Map<String,Object> obj = new HashMap<String,Object>();
				obj.put("id", product.getId());
				obj.put("productName", product.getProductName());
				obj.put("modelNumber", product.getModelNumber());
				obj.put("quantity", product.getQuantity());
				obj.put("productType", product.getProductType());
				obj.put("unitPrice", product.getUnitPrice());
				obj.put("discountRate", product.getDiscountRate());
				list.add(obj);
			}
		
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(list));
			}
		}
	}
	
	@RequestMapping(value = "/verifyPurchaseInvoice/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void verifyPurchaseInvoice(@RequestBody PurchaseInvoiceDTO purchaseInvoiceDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		CheckerController cc = new CheckerController();
		HashMap<String, String> user = cc.getCheckerMap();
		Object key = user.get(purchaseInvoiceDTO.getSessionId());
	
		Map<String,Object> obj = new HashMap<String,Object>();
		
		if(key != null){
			Object keyId = user.get("checkerId"+purchaseInvoiceDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Checker checker = checkerServices.getCheckerById(checkerId);
			if(checker != null){
				System.out.println(purchaseInvoiceDTO.isVerify());
				PurchaseInvoice purchaseInvoice = purchaseInvoiceServices.getPurchaseInvoiceById(purchaseInvoiceDTO.getInvoiceNo());
				if(purchaseInvoice != null){
				PurchaseInvoice pi = purchaseInvoiceServices.verifyPurchaseInvoice(purchaseInvoiceDTO.getInvoiceNo(), purchaseInvoiceDTO.isVerify());
				if(pi.isVerify()){
					//Supplier Report creation
					//check if supplier present in supplier report or not
					SupplierReport oldSR = supplierReportServices.getSupplierReportBySupplierId(pi.getSupplier().getId());
					if(oldSR != null){
						Set<PurchaseInvoice> piList = oldSR.getPurchaseInvoice();
						piList.add(pi);
						oldSR.setPurchaseInvoice(piList);
						//set supplier report to purchase invoice
						pi.setSupplierReport(oldSR);
						supplierReportServices.createOrUpdateSupplierReport(oldSR);
						purchaseInvoiceServices.addOrUpdatePurchaseInvoice(pi);
					}else{
						SupplierReport sr = new SupplierReport();
						Set<PurchaseInvoice> piList = new HashSet<PurchaseInvoice>();
						piList.add(pi);
						sr.setPurchaseInvoice(piList);
						//set supplier report to purchase invoice
						pi.setSupplierReport(sr);
						sr.setSupplier(pi.getSupplier());
						supplierReportServices.createOrUpdateSupplierReport(sr);
						purchaseInvoiceServices.addOrUpdatePurchaseInvoice(pi);
					}
					
					//Stock Report creation
					//check if product present in stock or not
					StockReport s = stockReportServices.getStockReportByProductId(pi.getProduct().getId());
					long units = 0;
					if(s != null){
						units = s.getUnits();
						units = pi.getQuantity();
						s.setUnits(units);
						stockReportServices.createOrUpdateStockReport(s);
					}else{
						//if product is not present in stock.
						//make new entry of product in stock report.
						StockReport newStockReport = new StockReport();
						newStockReport.setProduct(pi.getProduct());
						newStockReport.setUnits(pi.getQuantity());
						stockReportServices.createOrUpdateStockReport(newStockReport);
					}
				}
				obj.put("invoiceNo", pi.getInvoiceNo());
				obj.put("invoiceDate", pi.getInoviceDate());
				obj.put("productName", pi.getProduct().getProductName());
				obj.put("modelNo", pi.getProduct().getModelNumber());
				obj.put("quantity", pi.getQuantity());
				obj.put("unitPrice", pi.getUnitPrice());
				obj.put("discount", pi.getProduct().getDiscountRate());
				obj.put("discountAmount", pi.getDiscountAmount());
				obj.put("finalAmount", pi.getFinalAmount());
				String verify = null;
						if(pi.isVerify()){
							verify = "verified";
						}else{
							verify = "not verified";
						}
				obj.put("verify", verify);
			}
		}
			else{
				obj.put("verify", "fail");
				obj.put("reason", "purchase invoice is not present");
			}
		}
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
	}
	
	@RequestMapping(value = "/createSaleInvoice/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void createSaleInvoice(@RequestBody SaleInvoiceDTO saleInvoiceDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		CheckerController cc = new CheckerController();
		HashMap<String, String> user = cc.getCheckerMap();
		Object key = user.get(saleInvoiceDTO.getSessionId());
	
		Map<String,Object> obj = new HashMap<String,Object>();
		
		if(key != null){
			Object keyId = user.get("checkerId"+saleInvoiceDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Checker checker = checkerServices.getCheckerById(checkerId);
			if(checker != null){
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
							saleInvoice.setChecker(checker);
							
							
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
	
	
	@RequestMapping(value = "/customerReportList/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void customerReportList(@RequestBody SaleInvoiceDTO saleInvoiceDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		CheckerController cc = new CheckerController();
		HashMap<String, String> user = cc.getCheckerMap();
		Object key = user.get(saleInvoiceDTO.getSessionId());
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(key != null){
			Object keyId = user.get("checkerId"+saleInvoiceDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Checker checker = checkerServices.getCheckerById(checkerId);
			if(checker != null){
				
				Admin checkerAdmin = checker.getAdmin();
				
				List<Customer> customerList = customerServices.customerList(checkerAdmin.getId());
				
				List<CustomerReport> customerReportList = customerReportServices.getCustomerReportList();
				
				for (CustomerReport customerReport : customerReportList) {
					for (Customer c : customerList) {
						if(customerReport.getCustomer().getId() == c.getId()){
							Map<String,Object> obj = new HashMap<String,Object>();
							obj.put("customerReportId", customerReport.getCustomerReportId());
							
							obj.put("customerId", customerReport.getCustomer().getId());
							obj.put("customerContactNo", customerReport.getCustomer().getContactNo());
							obj.put("customerEmail", customerReport.getCustomer().getEmail());
							obj.put("customerName", customerReport.getCustomer().getName());
							obj.put("customerAddress", customerReport.getCustomer().getAddress());
							
							obj.put("saleInvoiceNo", customerReport.getSaleInvoice().getSaleInvoiceNo());
							obj.put("quantity", customerReport.getSaleInvoice().getQuantity());
							obj.put("saleInvoiceDate", customerReport.getSaleInvoice().getInvoiceDate());
							obj.put("discountedAmount", customerReport.getSaleInvoice().getDiscountedAmount());
							obj.put("finalAmount", customerReport.getSaleInvoice().getFinalAmount());
							
							obj.put("productId", customerReport.getSaleInvoice().getProduct().getId());
							obj.put("productName", customerReport.getSaleInvoice().getProduct().getProductName());
							obj.put("modelNumber", customerReport.getSaleInvoice().getProduct().getModelNumber());
							obj.put("productType", customerReport.getSaleInvoice().getProduct().getProductType());
							obj.put("unitPrice", customerReport.getSaleInvoice().getProduct().getUnitPrice());
							obj.put("discountRate", customerReport.getSaleInvoice().getProduct().getDiscountRate());
							
							list.add(obj);
						}
					}
					
				}
			}
		}
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(list));
	}
		
	
	@RequestMapping(value = "/saleInvoiceList/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void saleInvoiceList(@RequestBody SaleInvoiceDTO saleInvoiceDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		CheckerController cc = new CheckerController();
		HashMap<String, String> user = cc.getCheckerMap();
		Object key = user.get(saleInvoiceDTO.getSessionId());
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(key != null){
			Object keyId = user.get("checkerId"+saleInvoiceDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Checker checker = checkerServices.getCheckerById(checkerId);
			if(checker != null){
				List<SaleInvoice> saleInvoiceList = saleInvoiceServices.getSaleInvoiceList();
				
				Admin checkerAdmin = checker.getAdmin();
				
				List<Checker> checkerList = checkerServices.checkerList(checkerAdmin.getId());
				List<SalesPerson> salesPersonList = salesPersonServices.salesPersonList(checkerAdmin.getId());
				
				for (SaleInvoice saleInvoice : saleInvoiceList) {
					for (Checker c : checkerList) {
						if(saleInvoice.getChecker() != null && saleInvoice.getChecker().getId() == c.getId()){
							Map<String,Object> obj = new HashMap<String,Object>();
							obj.put("saleInvoiceNo", saleInvoice.getSaleInvoiceNo());
							obj.put("saleInvoiceDate", saleInvoice.getInvoiceDate());
						
							obj.put("customerId", saleInvoice.getCustomer().getId());
							obj.put("customerName", saleInvoice.getCustomer().getName());
							obj.put("customerContactNo", saleInvoice.getCustomer().getContactNo());
							obj.put("customerAddress", saleInvoice.getCustomer().getAddress());
							
							obj.put("productId", saleInvoice.getProduct().getId());
							obj.put("productName", saleInvoice.getProduct().getProductName());
							obj.put("modelNumber", saleInvoice.getProduct().getModelNumber());
							obj.put("productType", saleInvoice.getProduct().getProductType());
							obj.put("unitPrice", saleInvoice.getProduct().getUnitPrice());
							obj.put("discountedRate", saleInvoice.getProduct().getDiscountRate());
							
							obj.put("saleProductQuantity", saleInvoice.getQuantity());
							obj.put("discounteAmount", saleInvoice.getDiscountedAmount());
							obj.put("finalAmount", saleInvoice.getFinalAmount());
							obj.put("paymentMode", saleInvoice.getPaymentMode());
							
							list.add(obj);
						}
					}
					for (SalesPerson salesPerson : salesPersonList) {
						if(saleInvoice.getSalesPerson() != null && salesPerson.getId() == saleInvoice.getSalesPerson().getId()){
							Map<String,Object> obj = new HashMap<String,Object>();
							obj.put("saleInvoiceNo", saleInvoice.getSaleInvoiceNo());
							obj.put("saleInvoiceDate", saleInvoice.getInvoiceDate());
						
							obj.put("customerId", saleInvoice.getCustomer().getId());
							obj.put("customerName", saleInvoice.getCustomer().getName());
							obj.put("customerContactNo", saleInvoice.getCustomer().getContactNo());
							obj.put("customerAddress", saleInvoice.getCustomer().getAddress());
							
							obj.put("productId", saleInvoice.getProduct().getId());
							obj.put("productName", saleInvoice.getProduct().getProductName());
							obj.put("modelNumber", saleInvoice.getProduct().getModelNumber());
							obj.put("productType", saleInvoice.getProduct().getProductType());
							obj.put("unitPrice", saleInvoice.getProduct().getUnitPrice());
							obj.put("discountedRate", saleInvoice.getProduct().getDiscountRate());
							
							obj.put("saleProductQuantity", saleInvoice.getQuantity());
							obj.put("discounteAmount", saleInvoice.getDiscountedAmount());
							obj.put("finalAmount", saleInvoice.getFinalAmount());
							obj.put("paymentMode", saleInvoice.getPaymentMode());
							
							list.add(obj);
						}
					}
				}
			}
		}
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(list));
	}
	
	@RequestMapping(value = "/stockReportList/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void stockReportList(@RequestBody SaleInvoiceDTO saleInvoiceDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		CheckerController cc = new CheckerController();
		HashMap<String, String> user = cc.getCheckerMap();
		Object key = user.get(saleInvoiceDTO.getSessionId());
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(key != null){
			Object keyId = user.get("checkerId"+saleInvoiceDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Checker checker = checkerServices.getCheckerById(checkerId);
			if(checker != null){
				List<StockReport> stockReportList = stockReportServices.getStockReportList();
				
				for (StockReport stockReport : stockReportList) {
					Map<String,Object> obj = new HashMap<String,Object>();
					obj.put("stockReportId", stockReport.getId());
					
					obj.put("productId", stockReport.getProduct().getId());
					obj.put("productName", stockReport.getProduct().getProductName());
					obj.put("modelNumber", stockReport.getProduct().getModelNumber());
					obj.put("productType", stockReport.getProduct().getProductType());
					obj.put("unitPrice", stockReport.getProduct().getUnitPrice());
					obj.put("discountedRate", stockReport.getProduct().getDiscountRate());
					
					obj.put("stockAvailable", stockReport.getUnits());
					
					list.add(obj);
				}
			}
		}
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(list));
	}
			
	@RequestMapping(value = "/supplierReportList/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void supplierReportList(@RequestBody SaleInvoiceDTO saleInvoiceDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		CheckerController cc = new CheckerController();
		HashMap<String, String> user = cc.getCheckerMap();
		Object key = user.get(saleInvoiceDTO.getSessionId());
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(key != null){
			Object keyId = user.get("checkerId"+saleInvoiceDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Checker checker = checkerServices.getCheckerById(checkerId);
			if(checker != null){
				
				Admin checkerAdmin = checker.getAdmin();
				
				List<Supplier> supplierList = supplierServices.supplierList(checkerAdmin.getId());
				
				List<SupplierReport> supplierReportList = supplierReportServices.getSupplierReportList();
				
				
				for (SupplierReport supplierReport : supplierReportList) {
					for (Supplier supplier : supplierList) {
						if(supplier.getId() == supplierReport.getSupplier().getId()){
							Map<String,Object> obj = new HashMap<String,Object>();
							
							PurchaseInvoice pi = purchaseInvoiceServices.getPurchaseInvoiceBySupplierReportId(supplierReport.getSupplierReportId());
							
							obj.put("supplierReportId", supplierReport.getSupplierReportId());
							
							obj.put("purchaseInvoiceNo", pi.getInvoiceNo());
							obj.put("", pi.getInoviceDate());
							
							obj.put("productId", pi.getProduct().getId());
							obj.put("productName", pi.getProduct().getProductName());
							obj.put("modelNumber", pi.getProduct().getModelNumber());
							obj.put("productType", pi.getProduct().getProductType());
							obj.put("discountedRate", pi.getProduct().getDiscountRate());
							
							obj.put("unitPrice", pi.getUnitPrice());
							obj.put("quantity", pi.getQuantity());
							obj.put("discountAmount", pi.getDiscountAmount());
							obj.put("finalAmount", pi.getFinalAmount());
							
							obj.put("supplierId", supplierReport.getSupplier().getId());
							obj.put("supplierName", supplierReport.getSupplier().getName());
							obj.put("supplierContactNo", supplierReport.getSupplier().getContactNo());
							obj.put("supplierEmail", supplierReport.getSupplier().getEmail());
							obj.put("supplierAddress", supplierReport.getSupplier().getAddress());
							
							list.add(obj);
						}
					}
				}
			}
		}
		
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(list));
	}
	
	@RequestMapping(value = "/taxInvoiceList/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void taxInvoiceList(@RequestBody SaleInvoiceDTO saleInvoiceDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		CheckerController cc = new CheckerController();
		HashMap<String, String> user = cc.getCheckerMap();
		Object key = user.get(saleInvoiceDTO.getSessionId());
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(key != null){
			Object keyId = user.get("checkerId"+saleInvoiceDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Checker checker = checkerServices.getCheckerById(checkerId);
			if(checker != null){
				List<TaxInvoice> taxInvoiceList = taxInvoiceServices.getTaxInvoiceList();
				
				for (TaxInvoice taxInvoice : taxInvoiceList) {
					Map<String,Object> obj = new HashMap<String,Object>();
					
					obj.put("taxInvoiceNo", taxInvoice.getTaxInvoiceNo());
					
					obj.put("saleInvoiceNo", taxInvoice.getSaleInvoice().getSaleInvoiceNo());
					obj.put("saleInvoiceDate", taxInvoice.getSaleInvoice().getInvoiceDate());
				
					obj.put("customerId", taxInvoice.getSaleInvoice().getCustomer().getId());
					obj.put("customerName", taxInvoice.getSaleInvoice().getCustomer().getName());
					obj.put("customerContactNo", taxInvoice.getSaleInvoice().getCustomer().getContactNo());
					obj.put("customerAddress", taxInvoice.getSaleInvoice().getCustomer().getAddress());
					
					obj.put("productId", taxInvoice.getSaleInvoice().getProduct().getId());
					obj.put("productName", taxInvoice.getSaleInvoice().getProduct().getProductName());
					obj.put("modelNumber", taxInvoice.getSaleInvoice().getProduct().getModelNumber());
					obj.put("productType", taxInvoice.getSaleInvoice().getProduct().getProductType());
					obj.put("unitPrice", taxInvoice.getSaleInvoice().getProduct().getUnitPrice());
					obj.put("discountedRate", taxInvoice.getSaleInvoice().getProduct().getDiscountRate());
					
					obj.put("saleProductQuantity", taxInvoice.getSaleInvoice().getQuantity());
					obj.put("discounteAmount", taxInvoice.getSaleInvoice().getDiscountedAmount());
					obj.put("finalAmount", taxInvoice.getSaleInvoice().getFinalAmount());
				
					list.add(obj);
				}
			}
		}
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(list));
	}
	
	public HashMap<String, String> getCheckerMap() {    
	    return checkerMap;
	}
}
