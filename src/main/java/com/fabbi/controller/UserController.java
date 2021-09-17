package com.fabbi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fabbi.dto.UserCreateDTO;
import com.fabbi.dto.UserUpdateDTO;
import com.fabbi.service.StaffService;

@Controller
public class UserController {

	@Autowired
	private StaffService staffService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/users/{pageNo}")
	public String index(@PathVariable(value = "pageNo") int pageNo, Model model) {
		int pageSize = 3;
		int tmp = staffService.count();
		int totalPage = 0;
		List<UserCreateDTO> userList = staffService.findPaginated(pageNo, pageSize);
		
		if (tmp < pageSize) {
			totalPage = 1;
		} else {
			if (tmp % pageSize == 0) {
				totalPage = tmp / pageSize;
			} else {
				totalPage = (tmp / pageSize) + 1;
			}
		}
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", totalPage);
		model.addAttribute("totalItems", userList.size());
		model.addAttribute("userList", userList);
		
		return "list-user";
	}
	
	@GetMapping("/users/search/{pageNo}")
	public String search(@PathVariable(value = "pageNo") int pageNo, @Param("keyword") String keyword, Model model) {
		
		int pageSize = 3;
		int tmp = 0;
		int totalPage = 0;
		List<UserCreateDTO> userList;
		
		if (keyword != null) {
			tmp = staffService.countByKeyword(keyword);
			userList = staffService.search(keyword, pageNo, pageSize);
			
			if (tmp < pageSize) {
				totalPage = 1;
			} else {
				if (tmp % pageSize == 0) {
					totalPage = tmp / pageSize;
				} else {
					totalPage = (tmp / pageSize) + 1;
				}
			}
			
		} else {
			tmp = staffService.count();
			userList = staffService.findPaginated(pageNo, pageSize);
			
			if (tmp < pageSize) {
				totalPage = 1;
			} else {
				if (tmp % pageSize == 0) {
					totalPage = tmp / pageSize;
				} else {
					totalPage = (tmp / pageSize) + 1;
				}
			}
		}
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", totalPage);
		model.addAttribute("totalItems", userList.size());
		model.addAttribute("userList", userList);
		model.addAttribute("keyword", keyword);
		
		return "list-user";
	}
	
	@GetMapping("/users/add-user")
    public String getAddForm(@ModelAttribute UserCreateDTO userDTO, Model model) {
		model.addAttribute("userDTO", userDTO);
        return "add-user";
    }
	
	@PostMapping("/users/add-user")
	public String addUser(@Valid UserCreateDTO userDTO, BindingResult bindingResult) {
		
		Boolean isExistByUsername = staffService.isUserExistByUsername(userDTO.getUsername());
		if (isExistByUsername) {
			bindingResult.addError(new FieldError("userDTO", "username", "Username already in use!"));
		}
		
		Boolean isExistByEmail = staffService.isUserExistByEmail(userDTO.getEmail());
		if (isExistByEmail) {
			bindingResult.addError(new FieldError("userDTO", "email", "Email address already in use!"));
		}
		
		if (bindingResult.hasErrors()) {
			return "add-user";
		}
		
		Boolean result = staffService.add(userDTO);
		
		if (!result) {
			bindingResult.addError(new FieldError("userDTO", "errorField", "Error to create User"));
			return "add-user";
		}
		
		return "redirect:/users/1";
	}
	
	@GetMapping("/users/edit-user/{id}")
	public String getEditForm(@PathVariable Integer id, Model model) {
		UserCreateDTO userDTO = staffService.getById(id);
		model.addAttribute("userDTO", userDTO);
		return "edit-user";
	}
	
	@PostMapping("/users/edit-user")
	public String updateUser(@Valid UserUpdateDTO userDTO, BindingResult bindingResult) {
		
		Boolean isExistByEmail = staffService.isUserExistByEmailAndIdNot(userDTO.getEmail(), userDTO.getId());
		if (isExistByEmail) {
			bindingResult.addError(new FieldError("userDTO", "email", "Email address already in use!"));
		}
		
		if (bindingResult.hasErrors()) {
			return "edit-user";
		}
		
		Boolean result = staffService.update(userDTO);
		
		if (!result) {
			bindingResult.addError(new FieldError("userDTO", "errorField", "Error to create User"));
			return "edit-user";
		}
		
		return "redirect:/users/1";
	}
	
	@GetMapping("/users/delete-user/{id}")
	public String deleteUser(@PathVariable Integer id) {
		
		Boolean result = staffService.delete(id);
		
		if (!result) {
			
		}
		
		return "redirect:/users/1";
	}
}
