package fr.lauparr.apigenerator.controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.lauparr.apigenerator.pojo.dto.ContentFieldSimpleDTO;
import fr.lauparr.apigenerator.pojo.dto.ContentSimpleDTO;
import fr.lauparr.apigenerator.pojo.vm.ContentFieldVM;
import fr.lauparr.apigenerator.pojo.vm.ContentVM;
import fr.lauparr.apigenerator.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

	@GetMapping("/{id}")
	public ContentSimpleDTO getContentById(@PathVariable Long id) {
		return contentService.getContentById(id);
	}

	@PostMapping
	public ContentSimpleDTO createContent(@RequestBody ContentVM body) {
		return this.contentService.createContent(body);
	}

	@PutMapping("/{id}")
	public ContentSimpleDTO updateContent(@PathVariable Long id, @RequestBody ContentVM body) {
		return this.contentService.updateContent(id, body);
	}

	@PutMapping("/{idContent}/show_fields")
	public ContentSimpleDTO updateShowFields(@PathVariable Long idContent, @RequestBody ArrayNode body) {
		return this.contentService.updateShowFields(idContent, body);
	}

	@DeleteMapping("/{id}")
	public void deleteContent(@PathVariable Long id) {
		this.contentService.deleteContent(id);
	}

	@PostMapping("/{idContent}/fields")
	public ContentFieldSimpleDTO addField(@PathVariable Long idContent, @RequestBody ContentFieldVM body) {
		return this.contentService.addField(idContent, body);
	}

	@PutMapping("/{idContent}/fields/{idField}")
	public ContentFieldSimpleDTO updateField(@PathVariable Long idContent, @PathVariable Long idField, @RequestBody ContentFieldVM body) {
		return this.contentService.updateField(idContent, idField, body);
	}

	@DeleteMapping("/fields/{idField}")
	public void deleteField(@PathVariable Long idField) {
		this.contentService.deleteField(idField);
	}

}
