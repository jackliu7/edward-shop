package com.edward.portal.controller;

import java.util.Map;

import com.edward.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

@RestController
@RequestMapping("/itemsearch")
public class ItemSearchController {
	
	@Reference
	private ItemSearchService itemSearchService;
	
	@RequestMapping("/search")
	public Map search(@RequestBody Map searchMap){
		return itemSearchService.search(searchMap);
		
	}

}
