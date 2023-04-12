package com.swe443.group4.gis.controller;

import com.swe443.group4.gis.pojo.Item;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

	
	@GetMapping("/")
	@ResponseBody
	public String abc() {
		return "hello world";
	}
	
	@RequestMapping("/test")
	public List<Item> getItems(){
		List<Item> list = new ArrayList<>();
		list.add( new Item(996L, "Pizza", "43",1L,"1",1.2,"CharityGroup1"));
		list.add( new Item(233L, "Darth", "67",0L,"1",2.2,null));
		list.add( new Item(666L, "Fries", "26",0L,"1",5.2,"somewhere in Africa"));
		return list;
	}
	
}
