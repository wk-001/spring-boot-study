package com.wk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class PageController {

	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		return page;
	}

	@RequestMapping("/toPage/{fold}/{page}")
	public String showPages(@PathVariable String fold, @PathVariable String page){
		String path = fold+"/"+page;
		return path;
	}

}
