package com.fabbi.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fabbi.constant.Constant;
import com.fabbi.dto.CategoryDTO;
import com.fabbi.dto.ProductDTO;
import com.fabbi.dto.SupplierDTO;
import com.fabbi.service.CategoryService;
import com.fabbi.service.ProductService;
import com.fabbi.service.SupplierService;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private SupplierService supplierService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/products/add-product")
	public String getAddForm(@ModelAttribute ProductDTO productDTO, Model model) {
		List<CategoryDTO> categoryList = categoryService.findAll();
		List<SupplierDTO> supplierList = supplierService.findAll();
		
		model.addAttribute("productDTO", productDTO);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("supplierList", supplierList);
		
		return "add-product";
	}
	
	@PostMapping("/products/add-product")
	public String addProduct(@Valid ProductDTO productDTO
			, @RequestParam("thumbnailImage") MultipartFile multipartFile
			, BindingResult bindingResult
			, RedirectAttributes redirectAttributes) throws IOException {
		
		String name = productDTO.getName();
		Integer supplierId = productDTO.getSupplierId();
		Integer categoryId = productDTO.getCategoryId();
		
		Boolean isExistBySupplier = productService.isExistByNameAndSupplierId(name, supplierId);
		
		if (isExistBySupplier) {
			bindingResult.addError(new FieldError("productDTO", "name", "Existed product with same name and supplier!"));
			return "add-product";
		}
		
		Boolean isExistByCategory = productService.isExistByNameAndCategoryId(name, categoryId);
		
		if (isExistByCategory) {
			bindingResult.addError(new FieldError("productDTO", "name", "Existed product with same name and category!"));
			return "add-product";
		}
		
		if (bindingResult.hasErrors()) {
			return "add-product";
		}
		
		String raw = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		String filePrefix = raw.substring(raw.indexOf("."));
		
		String filename = name + "_" + supplierId + "_" + categoryId + filePrefix;
		
		productDTO.setThumbnail(filename);
		
		Boolean result = productService.add(productDTO);
		
		if (result) {
			Path uploadPath = Paths.get(Constant.UPLOAD_DIR);
			
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			try (InputStream inputStream = multipartFile.getInputStream()) {
				Path filePath = uploadPath.resolve(filename);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new IOException("Could not save uploaded file: [" + filename + "]");
			}
		} else {
			redirectAttributes.addFlashAttribute("messageFail", "Failed to save.");
		}
		
		redirectAttributes.addFlashAttribute("messageSuccess", "Saved successfully.");
		
		return "redirect:/products/1";
	}
}
