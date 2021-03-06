package com.target.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.target.constants.TargetConstnats;
import com.target.dto.CategoryDto;
import com.target.dto.ResponseData;
import com.target.service.CategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(final CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasAuthority('Admin')")
	public ResponseData<CategoryDto> addCategory(@ModelAttribute("categoryDto") final CategoryDto categoryDto) {
		Map<String, Object> result = categoryService.addCategory(categoryDto);
		ResponseData<CategoryDto> responseData = extractStatus(result);
		result.computeIfPresent("Data", (K, V) -> {
			responseData.setData((CategoryDto) V);
			return V;
		});
		return responseData;
	}

	@PostMapping(value = { "/update" })
	@PreAuthorize("hasAuthority('Admin')")
	public ModelAndView updateCategory(@ModelAttribute("categoryDto") final CategoryDto categoryDto) {
		Map<String, Object> result = categoryService.updateCategory(categoryDto);
		ModelAndView mav = new ModelAndView("categorySearchresult");
		ResponseData<CategoryDto> responseData = extractStatus(result);
		result.computeIfPresent("Data", (K, V) -> {
			List<CategoryDto> data = new ArrayList<>();
			data.add((CategoryDto) V);
			responseData.setListOfData(data);
			return V;
		});
		mav.addObject("result", responseData);
		return mav;
	}

	@PostMapping(value = { "/search" })
	public ModelAndView search(@ModelAttribute("categoryDto") final CategoryDto categoryDto) {
		ModelAndView mav = new ModelAndView("categorySearchresult");
		ResponseData<CategoryDto> responseData = new ResponseData<>();
		responseData.setStaus(TargetConstnats.SCUCCESS);
		responseData.setListOfData(categoryService.searchCategory(categoryDto));
		mav.addObject("result", responseData);
		return mav;
	}

	@GetMapping(value = { "/{id}" })
	public void getCategory(@PathVariable("id") final Integer id) {
		categoryService.getCategory(id);
	}

	@GetMapping(value = { "/all" })
	public void getAllCategory() {
		categoryService.getAllCategory();
	}

	private ResponseData<CategoryDto> extractStatus(Map<String, Object> payload) {
		return new ResponseData<>(
				payload.containsKey(TargetConstnats.SCUCCESS) ? TargetConstnats.SCUCCESS : TargetConstnats.ERROR,
				(payload.containsKey(TargetConstnats.SCUCCESS) ? (String) payload.get(TargetConstnats.SCUCCESS)
						: (String) payload.get(TargetConstnats.ERROR)));
	}
}
