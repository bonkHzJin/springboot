package com.swe443.gis.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.swe443.gis.entity.Item;
import com.swe443.gis.repository.ItemRepository;
import com.swe443.gis.service.GHSService;

/**
 * @author: Daniel Reynoso-Ramirez
 *
 */
@Controller
public class ItemGISController {

	// Autowired ItemRepository class
	@Autowired
	private ItemRepository IRepo; 
	
	/**
     * Handle GET request for "/test" endpoint
     *
     * @return a String response
     */
	@GetMapping("/test")
	@ResponseBody
	public String test() {
		return "This is GIS";
	}
	
	/**
     * Handle GET request for "/gis-add" endpoint
     *
     * @return a ModelAndView object that renders "gis-add-form" view
     */
	@GetMapping("/gis-add")
	public ModelAndView gisAdd() {
		ModelAndView mav = new ModelAndView("gis-add-form");
	    Item newItem = new Item();
		mav.addObject("item", newItem);
		return mav;
	}
	
	/**
     * Handle POST request for "/addItem" endpoint
     * Saves item into the database and redirects to a table of all items in inventory
     *
     * @param item an Item object that will be saved into the database
     * @return a String response that redirects to "/showGISItems" endpoint
     */
	@PostMapping("/addItem")
	public String saveItem(@ModelAttribute Item item) {
		IRepo.save(item);
		return "redirect:/showGISItems";
	}
	
	/**
     * Handle GET request for "/showGISItems" endpoint
     * This method shows all items that are currently in the GIS's database
     *
     * @return a ModelAndView object that renders "list-gisitems" view
     */
	@GetMapping("/showGISItems")
	public ModelAndView showGISItems() {
		ModelAndView mav = new ModelAndView("list-gisitems");
		List<Item> list = IRepo.findAll();
		mav.addObject("item", list);
		return mav;
	}
	
	 /**
     * Handle GET request for "/addItemForm" endpoint
     *
     * @return a ModelAndView object that renders "gis-add-form" view
     */
	@GetMapping("/addItemForm")
	public ModelAndView addItemForm() {
		ModelAndView mav = new ModelAndView("gis-add-form");
		Item newItem = new Item();
		mav.addObject("item", newItem);
		return mav;
	}
	
	/**
     * Handle GET request for "/search" endpoint
     * This method is used to search items based on a keyword
     *
     * @param keyword a String that is used to search for items
     * @return a ModelAndView object that renders "list-gisitems" view
     */
	@GetMapping("/search")
	public ModelAndView search(String keyword) {
		ModelAndView mav = new ModelAndView("list-gisitems");
		List<Item> list = new ArrayList<>();
		if(keyword!=null && !keyword.isBlank()) {
			list = IRepo.findByUpc(Long.parseLong(keyword));
		} else {
			return showGISItems();
		}	
		mav.addObject("item", list);
		return mav;
	}
	
	/**
	* Adds a new item to the GIS inventory.
	* @param The item to be added, obtained from the request body.
	* @return A message indicating whether the item was successfully added or already exists.
	*/
	@PostMapping("/api/addItemGCA")
	@ResponseBody
	public String addNewItem(@RequestBody Item foo) {
		if(foo.getUpc() != null && foo.getUpc() != -1 && foo != null) {
			List<Item> list = new ArrayList<>();
			list = IRepo.findByUpc(foo.getUpc());
			if(list.size() == 0) {
				IRepo.save(foo);
				return "Item did not exist and has been added";
			}
			return "Item is already in inventory";
		} 
			return "Item format is invaild";
	}
	
	/**
	 * Deletes an item from the GIS inventory.
	 * @param upc The UPC code of the item to be deleted, obtained from the request parameters.
	 * @return A redirect to the showGHSItems page.
	 */
	@GetMapping("/remove")
	public String remove(@RequestParam Long upc) {
		IRepo.deleteById(upc);
		return "redirect:/showGISItems";
	}
	
	/**
	 * Redirects to the simResults page.
     * @return A ModelAndView object for the simResults page.
	 */
	@GetMapping("/simRedirect")
	public ModelAndView simRedirect() {
		ModelAndView mav = new ModelAndView("simResults");
		return mav;
	}
	
	/**
	 * Simulates the expiration of items in the GIS inventory.
	 * @param simuDays The number of days to be simulated, obtained from the request parameters.
	 * @return A ModelAndView object for the simResults page with a message indicating the simulation status.
	 */
	@PostMapping("/expireSimulation")
	public ModelAndView expireSimulation(@RequestParam long simuDays) {
		ModelAndView mav = new ModelAndView("simResults");
		
		List<Item> wholeList = IRepo.findAll();
		
		for(int i = 0; i<wholeList.size();i++) {
			long temp = wholeList.get(i).getExpirationDate() - simuDays;
			System.out.println("\n" + temp + "\n");

			if(temp <= 0){
			 temp = 0;
			}
			wholeList.get(i).setExpirationDate(temp);

		}
		IRepo.saveAll(wholeList);
		
		mav.addObject("simuDayStatus","Successfully Simulated Days");
		return mav;
	}
	
	//dependency used to communicate and access GHS
	@Autowired
	private GHSService ghs;
	
	/**
	 * filters  all items in the GIS inventory that have passed
	 * The markdown threshold (3) or have already expired
	 * @return A list of all items at or nearing expiration.
	 */
	@GetMapping("/getGisItems")
	@ResponseBody
	public List<Item> getGisItems() {
		
		Long markdownThreshold = (long)3;
		List<Item> wholeList = IRepo.findAll();
		List<Item> undesirableList = new ArrayList<Item>();
		for(int i = 0; i < wholeList.size();i++) {
			if(wholeList.get(i).getExpirationDate() <= markdownThreshold) {
				wholeList.get(i).setFlag((long)0);
				undesirableList.add(wholeList.get(i));
				IRepo.delete(wholeList.get(i));
			}
		}

		return undesirableList;
	}
	
	
	/**
	 * this is a method to call GHS's method to obtain a List of 
	 * items that should haven handled by GIS, it will call method from
	 * service package and add them into the GIS's DB in tbl_gisitems
	 */
	public void getItems() {
		List<Item> temp = this.ghs.getItems();
		for(int i = 0; i < temp.size();i++) {
			IRepo.save(temp.get(i));
		}
	}
	
	/**
	 * this is what links to the "Request Items From GHS" button in the 
	 * HTML file from resource folder called "list-gisitems.html"
	 * @return it redirects/refresh the showGISItem page
	 */
	@GetMapping("/getGHSItems")
	public String saveToDB() {
		this.getItems();
		return "redirect:/showGISItems";
	}

}
