package com.swe443.group4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.swe443.group4.entity.Item;

/**
 * the plan was to just save it directly into the DB
 * but prof said he's gonna grade on microservices
 * so here it is.
 * 
 * 
 * 
 * 
 * note :
 * If you were to do it without Eureka, that's fine
 * but you will have to hard code the url
 * 
 * for example
 * 
 * just create a String object
 * String tmp = "http://localhost:THEPORT/THEADDRESS";
 * which is probably a lot easier.
 * 
 * @author pashhy
 *
 */
@Service
public class GISService {
	
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	
	/**
	 * this method calls method from other springboot package 
	 * that can be found on the Eureka Server
	 * @return list of items obtained from GIS
	 */
	@RequestMapping("/test1")
	public List<Item> getItems(){
		// Service Instance Object is packing basic service info such as :
		// IP, Port, etc.
		ServiceInstance si = this.loadBalancerClient.choose("Inventory-SERVICE");
		StringBuffer sb = new StringBuffer();
		
		// set up the link
		// FIXME : change the /test in the string buffer once the REAL GIS is finished
		sb.append("http://").append(si.getHost()).append(":").append(si.getPort()).append("/test");
		// same as sb = http://localhost:????/test
		
		//use springMVC Rest Template
		RestTemplate rt = new RestTemplate();
		
		
		// don't ask me how this works, idk either
		ParameterizedTypeReference <List<Item>> type = new ParameterizedTypeReference<List<Item>>() {};
		ResponseEntity<List<Item>> response = rt.exchange(sb.toString(),HttpMethod.GET,null, type);
		
		List<Item> list = response.getBody();
		
		return list;
	}
}
