package fr.lauparr.apigenerator.controllers;

import fr.lauparr.apigenerator.ApiGeneratorApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app")
public class AppController {

	@PostMapping("/restart")
	public void restart() {
		ApiGeneratorApplication.restart();
	}

}
