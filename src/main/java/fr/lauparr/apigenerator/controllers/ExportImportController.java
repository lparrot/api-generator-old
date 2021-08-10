package fr.lauparr.apigenerator.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.lauparr.apigenerator.services.ExportImportService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ExportImportController {

	@Autowired
	private ExportImportService exportImportService;
	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping("/exports/contents")
	public ObjectNode exportContents(@RequestParam(required = false, defaultValue = "false") Boolean full) {
		return exportImportService.exportContents(full);
	}

	@SneakyThrows
	@PostMapping("/imports/contents")
	public void importContents(MultipartFile file) {
		exportImportService.importContents(file);
	}
}
