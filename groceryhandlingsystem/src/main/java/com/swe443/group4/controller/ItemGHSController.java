package com.swe443.group4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.swe443.group4.entity.Item;
import com.swe443.group4.repository.ItemRepository;
import com.swe443.group4.service.GISService;

@Controller
public class ItemGHSController {

	@Autowired
	private ItemRepository IRepo; 
	// repository extends jpa, used for find(),save(),findbyid() etc.
	
	
	
	
	// test
	/**
	 * just to make sure ports and stuff is working
	 * @return
	 */
	@GetMapping("/test")
	@ResponseBody
	public String test() {
		return "This is GHS";
	}
	
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
	
	/**
	 * This allows users to directly add items when needed
	 * but it's mainly used for debugging, so that I don't have
	 * to add stuff from MySQL workbench or typing insert commands
	 * @return
	 */
	@GetMapping("/addItemForm")
	public ModelAndView addItemForm() {
		ModelAndView mav = new ModelAndView("add-item-form");
		Item newItem = new Item();
		mav.addObject("item", newItem);
		return mav;
	}
	
	/**
	 * linked to the save item from 
	 * btn save --from--> Add Items --from--> "add-item-form.html"
	 * @param item
	 * @return 
	 */
	@PostMapping("/saveItem")
	public String saveItem(@ModelAttribute Item item) {
		IRepo.save(item);
		return "redirect:/showGHSItems";
	}
	
	/**
	 * simple update method after modifying items
	 * @param upc
	 * @return
	 */
	@GetMapping("/showUpdateForm")
	public ModelAndView showUpdateForm(@RequestParam Long upc) {
		ModelAndView mav = new ModelAndView("add-item-form");
		Item item = IRepo.findById(upc).get();
		mav.addObject("item", item);
		return mav;
	}
	
	/**
	 * this provides a fast way of deleting items from the DB
	 * @param upc this will be obtained by Thymeleaf.
	 * @return go back to showGHSItems page
	 */
	@GetMapping("/disposal")
	public String Disposal(@RequestParam Long upc) {
		IRepo.deleteById(upc);
		return "redirect:/showGHSItems";
	}
	
	/**
	 * both methods below are for micro services
	 */
	@Autowired
	private GISService gis;
	
	/**
	 * this is a method to call GIS's method to obtain a List of 
	 * items that should be handled by GHS, it will call method from
	 * service package and add them into the GHS's DB in tbl_ghsitems
	 */
	@RequestMapping("/request")
	public void getItems() {
		List<Item> temp = this.gis.getItems();
		for(int i = 0; i < temp.size();i++) {
			IRepo.save(temp.get(i));
		}
	}
	
	/**
	 * this is what links to the "Request Items From GIS" button in the 
	 * HTML file from resource folder called "list-ghsitems.html"
	 * @return it redirects/refresh the showGHSItem page
	 */
	@GetMapping("/getItems")
	public String saveToDB() {
		this.getItems();
		return "redirect:/showGHSItems";
	}
	
	/**
	 * This method should be called by GIS
	 * GHS will get all the items
	 * if an item's attribute flag is >= 1
	 * then this item is marked down already.
	 * @return
	 */
	@SuppressWarnings("null")
	@GetMapping("/forDaniel")
	public List<Item> forGIS(){
		List<Item> tmpList = IRepo.findAll();
		List<Item> forReturn = null;
		for (Item it : tmpList) {
			if (it.getFlag() >= 1) {
				forReturn.add(it);
			}
		}
		// before sending it back to GIS, delete it from DB
		for (Item ti : forReturn) {
			IRepo.deleteById(ti.getUpc()); 
		}
		
		// then send it to GIS
		return forReturn;
	}
		

}
