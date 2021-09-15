package com.fabbi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fabbi.dto.UserDTO;
import com.fabbi.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/page/{pageNo}")
	public String index(@PathVariable(value = "pageNo") int pageNo, Model model) {
		int pageSize = 10;
		int totalPage;
		List<UserDTO> userList = userService.findPaginated(pageNo, pageSize);
		
		if (userList.size() %  pageSize == 0) {
			totalPage = userList.size() /  pageSize;
		} else {
			totalPage = (userList.size() /  pageSize) + 1;
		}
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", totalPage);
		model.addAttribute("totalItems", userList.size());
		model.addAttribute("userList", userList);
		
		return "";
	}
	
	@GetMapping("/add-user")
    public String getAddForm() {
        return "add-user";
    }
}
