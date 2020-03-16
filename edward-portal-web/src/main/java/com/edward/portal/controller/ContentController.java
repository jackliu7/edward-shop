package com.edward.portal.controller;

import java.util.List;

import com.edward.pojo.TbContent;
import com.edward.service.ContentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

@RestController
@RequestMapping("/content")
public class ContentController {

	@Reference
	private ContentService contentService;
	
	@RequestMapping("/findByCategoryId")
	public List<TbContent> findByCategoryId(Long categoryId){
		return contentService.findByCategoryId(categoryId);
	}
}
