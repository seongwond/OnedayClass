package com.springboot.AoooA.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.AoooA.dto.ImgDTO;
import com.springboot.AoooA.service.CategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public String categoryPage(Model model) {
		List<ImgDTO> categoryList = categoryService.getClassDetails("", "recent", 0, 12);
		model.addAttribute("categoryList", categoryList);
		return "category";
	}

	@GetMapping("/area/{area}")
	public String getCategoryByArea(@PathVariable("area") String area, Model model) {
		List<ImgDTO> categoryList = categoryService.getClassByArea(area, "", "recent", 0, 12);
		model.addAttribute("categoryList", categoryList);
		return "category";
	}

	@GetMapping("/type/{type}")
	public String getCategoryByType(@PathVariable("type") Integer type, Model model) {
		List<ImgDTO> categoryList = categoryService.getClassByType(type, "", "recent", 0, 12);
		model.addAttribute("categoryList", categoryList);
		return "category";
	}

	@GetMapping("/data")
	@ResponseBody
	public Map<String, Object> getCategoryData(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "sortType", defaultValue = "recent") String sortType,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "area", required = false) String area,
			@RequestParam(value = "type", required = false) Integer type) {

		final int PAGE_SIZE = 12;
		int offset = (page - 1) * PAGE_SIZE;

		List<ImgDTO> categoryList;
		int totalClassCount;

		if (area != null && !area.isEmpty()) {
			categoryList = categoryService.getClassByArea(area, keyword, sortType, offset, PAGE_SIZE);
			totalClassCount = categoryService.getTotalClassCountByAreaAndKeyword(area, keyword);
		} else if (type != null) {
			categoryList = categoryService.getClassByType(type, keyword, sortType, offset, PAGE_SIZE);
			totalClassCount = categoryService.getTotalClassCountByTypeAndKeyword(type, keyword);
		} else {
			categoryList = categoryService.getClassDetails(keyword, sortType, offset, PAGE_SIZE);
			totalClassCount = categoryService.getTotalClassCountByKeyword(keyword);
		}

		int totalPages = (int) Math.ceil((double) totalClassCount / PAGE_SIZE);

		Map<String, Object> response = new HashMap<>();
		response.put("categoryList", categoryList);
		response.put("totalPages", totalPages);
		response.put("currentPage", page);

		return response;
	}
}
