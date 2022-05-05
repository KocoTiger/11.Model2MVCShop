package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;






	@RestController
	//jason data�� �䱸�ϴ� �ֵ鸸 ó���� �뵵
	@RequestMapping("/product/*")
	public class ProductRestController {
	
		
	///////////////////////2022.04.22 (��)//////////////////////////////////
	//@RequestMapping("/addProduct.do")
	//1.1 json�� �پ� ������ Data�� ��û�� �ϴ� �� => restController�� ������ �ϰڴٴ� ��
	//�ؿ� String�� model& View������ String ������ ���� �� String ������ �����̴�
	@RequestMapping( value="json/addProduct", method=RequestMethod.POST )
	public Product addProduct( @RequestBody Product product) throws Exception {
	
	//������ �Ƿ��´�				  @RequestParam("manuDate") String manuDate ) throws Exception {
	//replace("-", "")
	product.setManuDate(product.getManuDate().replace("-", ""));
	
	//���� sql �� ��¥ �޾ƿö� �߾��� ���� �̷��� �ٲ����
	//Data�� ������ Ȥ�� �ϱ� ���� json �־��ֱ�
	System.out.println("/product/json/addProduct : POST");
	//Business Logic
	productService.addProduct(product);
	
	//////////////////////////////////////////
	//return "redirect:/product/addProductView.jsp";
	
	return product;
	//�̰� ���� ���� ������ forward�� ���°� �´µ�
	/////////////////////////////////////////
	}
	
	////////////////////////2022.04.22 (��)/////////////////////////
	//2.1 �ص� ������ �Բ����� getProductTest_Codehaus()
	///////////////////////////////////////////////////////
	
	//F
	@Autowired
	@Qualifier("productServiceImpl") //package com.model2.mvc.service.product.impl;�� wiring
	ProductService productService;
	
	
	@RequestMapping( value="json/getProduct/{prodNo}", method=RequestMethod.GET )
	public Product getProduct( @PathVariable int prodNo ) throws Exception {
		
//		System.out.println("/getProduct.do");
		System.out.println("/product/getProduct : GET");
		
		//Business Logic
		Product product = productService.getProduct(prodNo);
	
		
		//�ؿ����� UI�� ������ �ʿ䰡 ���� �׷���
		//return "forward:/product/getProduct.jsp"; �̰��� Navigation => �� �ʿ� ����
		
		//Data�� �����ܸ� �ȴ�
		return product;
	}

///////////////////////2022.04.27//////////////////////////////////
////////////////GEt����� �ȵ�/////////////////////////////////////////	
	
	//	@RequestMapping("/updateProductView.do")
	//	public String updateProductView( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{
		@RequestMapping( value="json/updateProduct/{prodNo}", method=RequestMethod.GET )
		public Product updateProduct( @PathVariable int prodNo ) throws Exception{	
	
	//		System.out.println("/updateProductView.do");
			System.out.println("/product/json/updateProduct : GET");
			
			//Business Logic
			Product product = productService.getProduct(prodNo);
			// Model �� View ����
//			model.addAttribute("product", product);
			
//			return "forward:/product/updateProductView.jsp";
	//		return "redirect:/product/updateProduct.jsp"; 
			//�̷��� �ϴ� ��ǰ���� ��ư�� �ȴ����� => ��ΰ� updateProduct����� ���ٰ� �ű⿡ �ִ� fncupdate�� �ް� ��
			return product;
			
		}

////////////////GEt����� �ȵ�/////////////////////////////////////////	
	
///////////////////////2022.04.27//////////////////////////////////	
//	@RequestMapping("/updateProduct.do")
//	public String updateProduct( @ModelAttribute("product") Product product , Model model , HttpSession session) throws Exception{
	@RequestMapping( value="json/updateProduct", method=RequestMethod.POST )
	public Product updateProduct( @RequestBody Product product, HttpSession session) throws Exception{

	
//		System.out.println("/updateProduct.do");
		System.out.println("/product/json/updateProduct : POST");
		
		//Business Logic
		productService.updateProduct(product);
		
		if(session.getAttribute("product") != null) {
		
		int sessionId=((Product)session.getAttribute("product")).getProdNo();
		if(sessionId == (product.getProdNo())){
			session.setAttribute("product", product);
			}
		}
		
//		return "redirect:/getProduct.do?prodNo=" + product.getProdNo();
		return product;
	}

///////////////////////2022.04.27//////////////////////////////////
//	@RequestMapping("/listProduct.do")
//	public String listProduct( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{

	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	
	@RequestMapping( value="json/listProduct" )
	public Map listProduct( @RequestBody Search search , HttpServletRequest request) throws Exception{

//		System.out.println("/listProduct.do");
		System.out.println("/product/json/listProduct : GET / POST");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic ����
		Map<String , Object> map = productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model �� View ����
//		model.addAttribute("list", map.get("list"));
//		model.addAttribute("resultPage", resultPage);
//		model.addAttribute("search", search);
		map.put("list", map.get("list"));
		map.put("resultPage", resultPage);
		map.put("search", search);
		
//		return "forward:/product/listProduct.jsp";
		return map;
	}
	
	
	
	
	
	
	
/////////////////////////////////////////////////////////////////////////////	
	
	
	
	
	
	
	
	
}
