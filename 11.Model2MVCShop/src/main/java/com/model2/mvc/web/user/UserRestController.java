package com.model2.mvc.web.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;


//==> ȸ������ RestController
@RestController
@RequestMapping("/user/*")
public class UserRestController {
	
	///Field
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	//setter Method ���� ����
//////////////////////////////////////////////////////////////////////////////		
	public UserRestController(){
		System.out.println(this.getClass());
	}
//////////////////////////////////////////////////////////////////////////////
	@RequestMapping( value="json/getUser/{userId}", method=RequestMethod.GET )
	public User getUser( @PathVariable String userId ) throws Exception{
		
		System.out.println("/user/json/getUser : GET");

		//Business Logic
		return userService.getUser(userId);
	}
/////////////////////////////////////////////////////////////////////////////
	@RequestMapping( value="json/login", method=RequestMethod.POST )
	public User login(	@RequestBody User user,
									HttpSession session ) throws Exception{
	
		System.out.println("/user/json/login : POST");
		//Business Logic
		System.out.println("::"+user);
		User dbUser=userService.getUser(user.getUserId());
		
		if( user.getPassword().equals(dbUser.getPassword())){
			session.setAttribute("user", dbUser);
		}
		
		return dbUser;
	}
/////////////////////////////////////////////////////////////////////////////
	//2022.02.24 get������� �Ȱ͸� �ϴ� �����غ� (�̰� �³�?)
	//String���� Null �̶�� ����....
	//Pass!
	@RequestMapping( value="json/addUser", method=RequestMethod.POST )
	public User addUser(@RequestBody User user) throws Exception{
	
		System.out.println("/user/json/addUser : POST");
		
		//Business Logic
		userService.addUser(user);
//		return "redirect:/user/addUserView.jsp";
		//void Ÿ���̶� return�� ������ �׷� ���?
//		return userService.addUser(user); //�̷��� ��ٰ� �ȵ�. void�����ΰ� ����
		return user;
	}
////////////////////2022.04.26//////��԰�ͼ� ����/�� �𸣰ڴ�.////////////////////////
/////////////////////////////////////////////////////////////////////////////	
	@RequestMapping( value="json/checkDuplication", method=RequestMethod.POST )
	public Map checkDuplication( @RequestBody User user  ) throws Exception{
		
		System.out.println("/user/json/checkDuplication : POST");
		
		//Business Logic
		boolean result=userService.checkDuplication(user.getUserId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", user.getUserId());
		map.put("result", new Boolean(result));
		
		return map;
		
//		model.addAttribute("result", new Boolean(result));
//		model.addAttribute("userId", userId);

//		return "forward:/user/checkDuplication.jsp";
	}
//////////////////////2022.04.26/////////////////////////////////////	
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	
	@RequestMapping( value="json/listUser" )
	public Map listUser( @RequestBody Search search ) throws Exception{
		
		System.out.println("/user/json/listUser : GET / POST");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		search.setPageSize(pageSize);
		
		// Business logic ����
		Map<String , Object> map = userService.getUserList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model �� View ����
		map.put("list", map.get("list"));
		map.put("resultPage", resultPage);
		map.put("search", search);
		
//		return "forward:/user/listUser.jsp";
		return map;
		
	}
	
//////////////////////2022.04.26/////////////////////////////////////	
//////////////////��Ȧ��//////////////////////////////////////	
	@RequestMapping( value="json/updateUser/{userId}", method=RequestMethod.GET )
	public User updateUser( @PathVariable String userId ) throws Exception{

		System.out.println("/user/json/updateUser : GET");
		
		//Business Logic
		User user = userService.getUser(userId);
		// Model �� View ����
//		model.addAttribute("user", user);
		
//		return "forward:/user/updateUser.jsp";
		return user;
	}
	
	@RequestMapping( value="json/updateUser", method=RequestMethod.POST )
	public User updateUser(  @RequestBody User user , HttpSession session) throws Exception{

		System.out.println("/user/json/updateUser : POST");
		//Business Logic
		userService.updateUser(user);
		
		if(session.getAttribute("user") != null) {
		
			String sessionId=((User)session.getAttribute("user")).getUserId();
			if(sessionId.equals(user.getUserId())){
				session.setAttribute("user", user);
			}
		}
		
		return user;
	}	
	

	
	

	
/////////////////////////////////////////////////////////////////////////////	
//logout�� ���ص� �ɵ�?
/////////////////////////////////////////////////////////////////////////////
//	@Value("#{commonProperties['pageUnit']}")
//	int pageUnit;
//	@Value("#{commonProperties['pageSize']}")
//	int pageSize;
//	
//	@RequestMapping( value="json/listUser", method=RequestMethod.GET )
//	public String listUser( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
//		
//		System.out.println("/user/json/listUser : GET / POST");
//		
//		if(search.getCurrentPage() ==0 ){
//			search.setCurrentPage(1);
//		}
//		search.setPageSize(pageSize);
//		
//		// Business logic ����
//		Map<String , Object> map=userService.getUserList(search);
//		
//		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
//		System.out.println(resultPage);
//		
//		// Model �� View ����
//		model.addAttribute("list", map.get("list"));
//		model.addAttribute("resultPage", resultPage);
//		model.addAttribute("search", search);
//		
////		return "forward:/user/listUser.jsp";
//		return resultPage;
	}
//////////////////////////////����!!!!!!!!!///////////////////////////////////////////////
	
	
	
	
	
	
	
/////////////////////////////////////////////////////////////////////////////	
	