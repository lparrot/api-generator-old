package fr.lauparr.apigenerator.controllers;

import fr.lauparr.apigenerator.pojo.dto.ContentSimpleDTO;
import fr.lauparr.apigenerator.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
public class ContentController {

	@Autowired
	private ContentService contentService;

	@GetMapping
	public List<ContentSimpleDTO> getContents() {
		return contentService.getContents();
	}

	@GetMapping("{id}")
	public ContentSimpleDTO getContentById(@PathVariable Long id) {
		return contentService.getContentById(id);
	}

}
