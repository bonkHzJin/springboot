package com.swe443.group4.gis.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_GISitem")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // this should be generated instead of hand written i think, otherwise, delete this line
	private Long upc;				// unique identifier
	private String name;				
	private String expirationDate;	// kinda hope this is an integer? 

	private Long flag; 				// to not allow items being marked down multiple time
	private String type; 			// to figure out to dispose or donate or mark down 
	private Double price;			
	private String destination;		// maybe won't use?
	
	
	
	
	
	public Item() {
		super();
	}
	
	public Item(Long upc, String name, String expirationDate, Long flag, String type, Double price,
			String destination) {
		super();
		this.upc = upc;
		this.name = name;
		this.expirationDate = expirationDate;
		this.flag = flag;
		this.type = type;
		this.price = price;
		this.destination = destination;
	}
	public Long getUpc() {
		return upc;
	}
	public void setUpc(Long upc) {
		this.upc = upc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public Long getFlag() {
		return flag;
	}
	public void setFlag(Long flag) {
		this.flag = flag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}

	
}
