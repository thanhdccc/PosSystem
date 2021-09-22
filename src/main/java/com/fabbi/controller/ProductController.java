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
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.fabbi.util.RandomUtils;

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
		String filename = null;
		
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
		if (!raw.equals("")) {
			String prefix = raw.substring(raw.lastIndexOf("."));
			
			filename = RandomUtils.ramdonString() + "_" + name.hashCode() + "_" + categoryId + "_" + supplierId + prefix;
			
			productDTO.setThumbnail(filename);
		} else {
			productDTO.setThumbnail("");
		}
		
		Boolean result = productService.add(productDTO);
		
		if (result && !raw.equals("")) {
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
			
			redirectAttributes.addFlashAttribute("messageSuccess", "Saved successfully.");
			
		} else if (!result) {
			
			redirectAttributes.addFlashAttribute("messageFail", "Failed to save.");
		} else if (result && raw.equals("")) {
			
			redirectAttributes.addFlashAttribute("messageSuccess", "Saved successfully.");
		}
		
		return "redirect:/products/1";
	}
	
	@GetMapping("/products/edit-product/{id}")
	public String getEditForm(@PathVariable("id") Integer id, Model model) {
		ProductDTO productDTO = productService.getById(id);
		List<CategoryDTO> categoryList = categoryService.findAll();
		List<SupplierDTO> supplierList = supplierService.findAll();
		
		model.addAttribute("productDTO", productDTO);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("supplierList", supplierList);
		
		return "edit-product";
	}
	
	@PostMapping("/products/edit-product")
	public String updateProduct(@Valid ProductDTO productDTO
			, @RequestParam("thumbnailImage") MultipartFile multipartFile
			, BindingResult bindingResult
			, RedirectAttributes redirectAttributes) throws IOException {
		
		String name = productDTO.getName();
		Integer supplierId = productDTO.getSupplierId();
		Integer categoryId = productDTO.getCategoryId();
		Integer id = productDTO.getId();
		String filename = null;
		
		Boolean isExistBySupplierAndIdNot = productService.isExistByNameAndSupplierIdAndIdNot(name, supplierId, id);
		if (isExistBySupplierAndIdNot) {
			bindingResult.addError(new FieldError("productDTO", "name", "Existed product with same name and supplier!"));
			return "edit-product";
		}
		
		Boolean isExistByCategoryAndIdNot = productService.isExistByNameAndCategoryIdAndIdNot(name, categoryId, id);
		if (isExistByCategoryAndIdNot) {
			bindingResult.addError(new FieldError("productDTO", "name", "Existed product with same name and category!"));
			return "edit-product";
		}
		
		if (bindingResult.hasErrors()) {
			return "edit-product";
		}
		
		String raw = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		if (!raw.equals("")) {
			String prefix = raw.substring(raw.lastIndexOf("."));
			
			filename = RandomUtils.ramdonString() + "_" + name.hashCode() + "_" + categoryId + "_" + supplierId + prefix;
			
			productDTO.setThumbnail(filename);
		} else {
			productDTO.setThumbnail("");
		}
		
		Boolean result = productService.update(productDTO);
		
		if (result && !raw.equals("")) {
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
			
			redirectAttributes.addFlashAttribute("messageSuccess", "Updated successfully.");
			
		} else if (!result) {
			
			redirectAttributes.addFlashAttribute("messageFail", "Failed to update.");
		} else if (result && raw.equals("")) {
			
			redirectAttributes.addFlashAttribute("messageSuccess", "Updated successfully.");
		}
		
		return "redirect:/products/1";
	}
	
	@GetMapping("/products/{pageNo}")
	public String index(@PathVariable(value = "pageNo") int pageNo, Model model) {
		int pageSize = Constant.PAGE_SIZE;
		int tmp = productService.count();
		int totalPage = 0;
		
		List<ProductDTO> productList = productService.findPaginated(pageNo, pageSize);
		
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
		model.addAttribute("totalItems", productList.size());
		model.addAttribute("productList", productList);
		
		return "list-product";
	}
	
	@GetMapping("/products/search/{pageNo}")
	public String search(@PathVariable(value = "pageNo") int pageNo, @Param("keyword") String keyword, Model model) {
		int pageSize = Constant.PAGE_SIZE;
		int tmp = 0;
		int totalPage = 0;
		
		List<ProductDTO> productList;
		
		if (keyword != null) {
			tmp = productService.countByKeyword(keyword);
			productList = productService.search(keyword, pageNo, pageSize);
			
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
			tmp = productService.count();
			productList = productService.findPaginated(pageNo, pageSize);
			
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
		model.addAttribute("totalItems", productList.size());
		model.addAttribute("productList", productList);
		model.addAttribute("keyword", keyword);
		
		return "list-product";
	}
	
	@GetMapping("/products/delete-product/{id}")
	public String deleteCustomer(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		
		Boolean result = productService.delete(id);
		
		if (!result) {
			redirectAttributes.addFlashAttribute("messageFail", "Failed to delete.");
		} else {
			redirectAttributes.addFlashAttribute("messageSuccess", "Deleted successfully.");
		}
		
		return "redirect:/products/1";
	}
}
