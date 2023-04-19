package com.swe443.gis.service;

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

import com.swe443.gis.entity.Item;

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
public class GHSService {
	
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	
	/**
	 * this method calls method from other springboot package 
	 * that can be found on the Eureka Server
	 * @return list of items obtained from GHS
	*/
	public List<Item> getItems(){
		// Service Instance Object is packing basic service info such as :
		// IP, Port, etc.
		ServiceInstance si = this.loadBalancerClient.choose("GHS-SERVICE");
		StringBuffer sb = new StringBuffer();
		
		// set up the link
		sb.append("http://").append(si.getHost()).append(":").append(si.getPort()).append("/markedDownItems");
		
		//use springMVC Rest Template
		RestTemplate rt = new RestTemplate();
		
		ParameterizedTypeReference <List<Item>> type = new ParameterizedTypeReference<List<Item>>() {};
		ResponseEntity<List<Item>> response = rt.exchange(sb.toString(),HttpMethod.GET,null, type);
		
		List<Item> list = response.getBody();
		
		return list;
	}
}
