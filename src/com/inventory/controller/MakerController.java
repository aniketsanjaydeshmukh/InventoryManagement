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
import com.inventory.DTO.PurchaseInvoiceDTO;
import com.inventory.model.Maker;
import com.inventory.model.Product;
import com.inventory.model.PurchaseInvoice;
import com.inventory.model.Supplier;
import com.inventory.services.MakerServices;
import com.inventory.services.ProductServices;
import com.inventory.services.PurchaseInvoiceServices;
import com.inventory.services.SupplierServices;

import flexjson.JSONSerializer;

@Controller
@RequestMapping("/maker")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MakerController {

	@Autowired
	MakerServices makerServices;
	
	@Autowired
	ProductServices productServices;
	
	@Autowired
	SupplierServices supplierServices;
	
	@Autowired
	PurchaseInvoiceServices purchaseInvoiceServices;
	
	public static HashMap<String, String> makerMap = new HashMap<String, String>();
	
	@RequestMapping(value = "/login/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void login(@RequestBody AdminDTO user,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String,Object> obj = new HashMap<String,Object>();
		 String sessionId = null;
			if(makerServices.login(user))
			{	
				 HttpSession sessionn = request.getSession();
				 sessionId = sessionn.getId();
				 makerMap.put(sessionId, sessionId);
				 Maker checker1 = makerServices.getMakerByUsername(user.getUsername());
				 String strI = Long.toString((checker1.getId()));
				 String makerId = "makerId"+sessionId;
				 makerMap.put(makerId,strI);
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
		MakerController mc = new MakerController();
		HashMap<String, String> user = mc.getMakerMap();
		Object key = user.get(adminDTO.getSessionId());
		Map<String,Object> obj = new HashMap<String,Object>();
		if(key != null){
				user.remove(adminDTO.getSessionId());
				user.remove("makerId"+adminDTO.getSessionId());
				obj.put("logout", "successful");
		}else{
			obj.put("logout", "unsuccessful");
		}
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));	
	}
	
	@RequestMapping(value = "/createPurchaseInvoice/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void createPurchaseInvoice(@RequestBody PurchaseInvoiceDTO purchaseInvoiceDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String,Object> obj = new HashMap<String,Object>();
		MakerController mc = new MakerController();
		HashMap<String, String> user = mc.getMakerMap();
		Object key = user.get(purchaseInvoiceDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("makerId"+purchaseInvoiceDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Maker maker = makerServices.getMakerById(checkerId);
			
			if(maker != null){
			
				Product product = productServices.getProductById(purchaseInvoiceDTO.getProductId());
				if(product != null){
					Supplier supplier = supplierServices.getSupplierById(purchaseInvoiceDTO.getSupplierId());
						if(supplier!=null){
							PurchaseInvoice purchaseInvoice = new PurchaseInvoice(purchaseInvoiceDTO);
							purchaseInvoice.setProduct(product);
							purchaseInvoice.setSupplier(supplier);
						
							//calculate discounted price on one unit
							double discountRate = product.getDiscountRate();
							double unitRate = purchaseInvoiceDTO.getUnitPrice();
							double discountedPrice = (discountRate/100)*unitRate;
							
							//calculate total discounted price
							double totalDiscountedPrice = discountedPrice * purchaseInvoiceDTO.getQuantity();
							
							//calculate final payble amount
							double finalAmount = (purchaseInvoiceDTO.getQuantity()*purchaseInvoiceDTO.getUnitPrice()) - totalDiscountedPrice;
							
							purchaseInvoice.setDiscountAmount(totalDiscountedPrice);
							purchaseInvoice.setFinalAmount(finalAmount);
							purchaseInvoice.setMaker(maker);
							
								if(purchaseInvoiceServices.addOrUpdatePurchaseInvoice(purchaseInvoice)){
									obj.put("purchaseInvoice", "added");
								}else{
									obj.put("purchaseInvoice", "not added");
								}
						
							}else{
									obj.put("purchaseInvoice", "not added");
									obj.put("reason", "supplier is not present");
							}
				}else{
						obj.put("purchaseInvoice", "not added");
						obj.put("reason", "product is not present");
				}
			}else{
				obj.put("purchaseInvoice", "not added");
				obj.put("reason", "maker is not present");
			}
		}
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));	
	}
	
	//update purchase Invoice by Maker only checker dosen't verify it.
	@RequestMapping(value = "/updatePurchaseInvoice/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void updatePurchaseInvoice(@RequestBody PurchaseInvoiceDTO purchaseInvoiceDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String,Object> obj = new HashMap<String,Object>();
		MakerController mc = new MakerController();
		HashMap<String, String> user = mc.getMakerMap();
		Object key = user.get(purchaseInvoiceDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("makerId"+purchaseInvoiceDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Maker maker = makerServices.getMakerById(checkerId);
			if(maker != null){
				PurchaseInvoice oldPurchaseInvoice = purchaseInvoiceServices.getPurchaseInvoiceById(purchaseInvoiceDTO.getInvoiceNo());
				if(oldPurchaseInvoice != null){
					if(oldPurchaseInvoice.isVerify()){
						obj.put("purchaseInvoice", "not updated");
						obj.put("reason", "purchase invoice verified by checker");
					}else{
						Product product = productServices.getProductById(purchaseInvoiceDTO.getProductId());
						if(product != null){
							Supplier supplier = supplierServices.getSupplierById(purchaseInvoiceDTO.getSupplierId());
							if(supplier!=null){
								//PurchaseInvoice purchaseInvoice = new PurchaseInvoice(purchaseInvoiceDTO);
								oldPurchaseInvoice.setProduct(product);
								oldPurchaseInvoice.setSupplier(supplier);
								
								//calculate discounted price on one unit
								double discountRate = product.getDiscountRate();
								double unitRate = purchaseInvoiceDTO.getUnitPrice();
								double discountedPrice = (discountRate/100)*unitRate;
								
								//calculate total discounted price
								double totalDiscountedPrice = discountedPrice * purchaseInvoiceDTO.getQuantity();
								
								//calculate final payble amount
								double finalAmount = (purchaseInvoiceDTO.getQuantity()*purchaseInvoiceDTO.getUnitPrice()) - totalDiscountedPrice;
								
								oldPurchaseInvoice.setUnitPrice(purchaseInvoiceDTO.getUnitPrice());
								oldPurchaseInvoice.setQuantity(purchaseInvoiceDTO.getQuantity());
								oldPurchaseInvoice.setDiscountAmount(totalDiscountedPrice);
								oldPurchaseInvoice.setFinalAmount(finalAmount);
								
								if(purchaseInvoiceServices.addOrUpdatePurchaseInvoice(oldPurchaseInvoice)){
									obj.put("purchaseInvoice", "updated");
								}else{
									obj.put("purchaseInvoice", "not updated");
								}
							}else{
								obj.put("purchaseInvoice", "not updated");
								obj.put("reason", "supplier is not present");
							}
						}
						else{
							obj.put("purchaseInvoice", "not updated");
							obj.put("reason", "product is not present");
						}
					}
				}else{
					obj.put("purchaseInvoice", "not updated");
					obj.put("reason", "purchaseInvoice is not present");
				}
			}else{
				obj.put("purchaseInvoice", "not updated");
				obj.put("reason", "maker is not present");
			}
		}
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));	
	}
	
	
	@RequestMapping(value = "/deletePurchaseInvoice/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void deletePurchaseInvoice(@RequestBody PurchaseInvoiceDTO purchaseInvoiceDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String,Object> obj = new HashMap<String,Object>();
		MakerController mc = new MakerController();
		HashMap<String, String> user = mc.getMakerMap();
		Object key = user.get(purchaseInvoiceDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("makerId"+purchaseInvoiceDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Maker maker = makerServices.getMakerById(checkerId);
			if(maker != null){
				PurchaseInvoice oldPurchaseInvoice = purchaseInvoiceServices.getPurchaseInvoiceById(purchaseInvoiceDTO.getInvoiceNo());
				if(oldPurchaseInvoice != null){
					if(oldPurchaseInvoice.isVerify()){
						obj.put("purchaseInvoice", "not deleted");
						obj.put("reason", "purchase invoice verified by checker");
					}else{
						if(purchaseInvoiceServices.deletePurchaseInvoice(purchaseInvoiceDTO.getInvoiceNo())){
							obj.put("purchaseInvoice", "deleted");
						}else{
							obj.put("purchaseInvoice", "fail");
							obj.put("reason", "not present");
						}
					}
				}
			}
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
		}
	}
	
	@RequestMapping(value = "/listPurchaseInvoice/", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody
	void listPurchaseInvoice(@RequestBody PurchaseInvoiceDTO purchaseInvoiceDTO,HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		MakerController mc = new MakerController();
		HashMap<String, String> user = mc.getMakerMap();
		Object key = user.get(purchaseInvoiceDTO.getSessionId());
		if(key != null){
			Object keyId = user.get("makerId"+purchaseInvoiceDTO.getSessionId());
			long checkerId = Long.parseLong(keyId.toString());
			Maker maker = makerServices.getMakerById(checkerId);
			if(maker != null){
				List<PurchaseInvoice> customerList = purchaseInvoiceServices.getPurchaseList();
			
				List<Maker> makerList = makerServices.makerList(maker.getAdmin().getId());
				
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				
				for (PurchaseInvoice pi : customerList) {
					
					for (Maker m : makerList) {
						if(m.getId() == pi.getMaker().getId()){
							Map<String,Object> obj = new HashMap<String,Object>();
							obj.put("invoiceNo", pi.getInvoiceNo());
							obj.put("invoiceDate", pi.getInoviceDate());
							obj.put("productName", pi.getProduct().getProductName());
							obj.put("modelNo", pi.getProduct().getModelNumber());
							obj.put("quantity", pi.getQuantity());
							obj.put("unitPrice", pi.getUnitPrice());
							obj.put("discount", pi.getProduct().getDiscountRate());
							obj.put("discountAmount", pi.getDiscountAmount());
							obj.put("finalAmount", pi.getFinalAmount());
							list.add(obj);
						}
					}
				}
			
				response.setContentType("application/json; charset=UTF-8"); 
				response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(list));
			}
		}
	}
	
	public HashMap<String, String> getMakerMap() {    
	    return makerMap;
	}
}
