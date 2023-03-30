package com.swe443.group4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.swe443.group4.entity.Item;
import com.swe443.group4.repository.ItemRepository;

@Controller
public class ItemGHSController {

	@Autowired
	private ItemRepository IRepo;
	
	
	/**
	 * This method is used for showing all content that's currently sitting in 
	 * MySQL DB's table for GHS
	 * @return an model and view object
	 */
	@GetMapping("/showGHSItems")
	public ModelAndView showGHSItems() {
		
		ModelAndView mav = new ModelAndView("list-ghsitems");
		List<Item> list = IRepo.findAll();
		mav.addObject("item", list);
		
		return mav;
	}
	
	@GetMapping("/addItemForm")
	public ModelAndView addItemForm() {
		ModelAndView mav = new ModelAndView("add-item-form");
		Item newItem = new Item();
		mav.addObject("item", newItem);
		return mav;
	}
	
	@PostMapping("/saveItem")
	public String saveItem(@ModelAttribute Item item) {
		IRepo.save(item);
		return "redirect:/showGHSItems";
	}
	
	@GetMapping("/showUpdateForm")
	public ModelAndView showUpdateForm(@RequestParam Long upc) {
		ModelAndView mav = new ModelAndView("add-item-form");
		Item item = IRepo.findById(upc).get();
		mav.addObject("item", item);
		return mav;
	}
	
	@GetMapping("/disposal")
	public String Disposal(@RequestParam Long upc) {
		IRepo.deleteById(upc);
		return "redirect:/showGHSItems";
	}
	
}
