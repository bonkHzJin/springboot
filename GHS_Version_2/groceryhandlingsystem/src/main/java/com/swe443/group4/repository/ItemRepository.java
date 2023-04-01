package com.swe443.group4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.swe443.group4.entity.Item;


// to have predefined method that can be called in the controller.
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	/**
	 * this extends Spring JPA
	 * the point of this is to create repository object
	 * using such object will allow easy save/delete/findbyid/findall/etc
	 * check JavaDoc from spring.io for all methods
	 */
}
