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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fabbi.constant.Constant;
import com.fabbi.dto.CategoryDTO;
import com.fabbi.service.CategoryService;

@Controller
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/categories/{pageNo}")
	public String index(@PathVariable(value = "pageNo") int pageNo, Model model) {
		int pageSize = Constant.PAGE_SIZE;
		int tmp = categoryService.count();
		int totalPage = 0;
		
		List<CategoryDTO> categoryList = categoryService.findPaginated(pageNo, pageSize);
		
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
		model.addAttribute("totalItems", categoryList.size());
		model.addAttribute("categoryList", categoryList);
		
		return "list-category";
	}
	
	@GetMapping("/categories/search/{pageNo}")
	public String search(@PathVariable(value = "pageNo") int pageNo, @Param("name") String name, Model model) {
		int pageSize = Constant.PAGE_SIZE;
		int tmp = 0;
		int totalPage = 0;
		
		List<CategoryDTO> categoryList;
		
		if (name != null) {
			tmp = categoryService.countByName(name);
			categoryList = categoryService.search(name, pageNo, pageSize);
			
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
			tmp = categoryService.count();
			categoryList = categoryService.findPaginated(pageNo, pageSize);
			
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
		model.addAttribute("totalItems", categoryList.size());
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("name", name);
		
		return "list-category";
	}
	
	@GetMapping("/categories/add-category")
	public String getAddForm(@ModelAttribute CategoryDTO categoryDTO, Model model) {
		model.addAttribute("categoryDTO", categoryDTO);
		return "add-category";
	}
	
	@PostMapping("/categories/add-category")
	public String addCategory(@Valid CategoryDTO categoryDTO
			, BindingResult bindingResult
			, RedirectAttributes redirectAttributes) {
		
		Boolean isExistByName = categoryService.isExistByName(categoryDTO.getName());
		
		if (isExistByName) {
			bindingResult.addError(new FieldError("categoryDTO", "name", "Name already in use!"));
			return "add-category";
		}
		
		if (bindingResult.hasErrors()) {
			return "add-category";
		}
		
		Boolean result = categoryService.add(categoryDTO);
		
		if (!result) {
			redirectAttributes.addFlashAttribute("messageFail", "Failed to save.");
		} else {
			redirectAttributes.addFlashAttribute("messageSuccess", "Saved successfully.");
		}
		
		return "redirect:/categories/1";
	}
	
	@GetMapping("/categories/edit-category/{id}")
	public String getEditForm(@PathVariable Integer id, Model model) {
		CategoryDTO categoryDTO = categoryService.getById(id);
		model.addAttribute("categoryDTO", categoryDTO);
		return "edit-category";
	}
	
	@PostMapping("/categories/edit-category")
	public String updateCategory(@Valid CategoryDTO categoryDTO
			, BindingResult bindingResult
			, RedirectAttributes redirectAttributes) {
		
		Boolean isExistByNameAndIdNot = categoryService.isExistByNameAndIdNot(categoryDTO.getName(), categoryDTO.getId());
		
		if (isExistByNameAndIdNot) {
			bindingResult.addError(new FieldError("categoryDTO", "name", "Name already in use!"));
			return "edit-category";
		}
		
		if (bindingResult.hasErrors()) {
			return "edit-category";
		}
		
		Boolean result = categoryService.update(categoryDTO);
		
		if (!result) {
			redirectAttributes.addFlashAttribute("messageFail", "Failed to update.");
		} else {
			redirectAttributes.addFlashAttribute("messageSuccess", "Updated successfully.");
		}
		
		return "redirect:/categories/1";
	}
	
	@GetMapping("/categories/delete-category/{id}")
	public String deleteCatogory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		Boolean result = categoryService.delete(id);
		
		if (!result) {
			redirectAttributes.addFlashAttribute("messageFail", "Failed to delete.");
		} else {
			redirectAttributes.addFlashAttribute("messageSuccess", "Deleted successfully.");
		}
		
		return "redirect:/categories/1";
	}
}
