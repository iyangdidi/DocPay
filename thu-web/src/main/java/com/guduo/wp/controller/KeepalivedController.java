package com.guduo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class KeepalivedController {

	@RequestMapping("/keepalived")
	public String keepalived() {
		return "OK";
	}
}
