package fr.lauparr.apigenerator.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import fr.lauparr.apigenerator.services.EntryPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/data/{slug}")
public class EntryPointController {

	@Autowired
	private EntryPointService entryPointService;

	@GetMapping
	public ResponseEntity<JsonNode> entryGet(@PathVariable String slug, Pageable page) {
		return ResponseEntity.ok(JsonNodeFactory.instance.pojoNode(entryPointService.getData(slug, page)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<JsonNode> entryGetId(@PathVariable String slug, @PathVariable String id) {
		return ResponseEntity.ok(JsonNodeFactory.instance.pojoNode(entryPointService.getDataById(slug, id)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<JsonNode> entryPut(@PathVariable String slug, @PathVariable String id, @RequestBody(required = false) JsonNode body) {
		return ResponseEntity.ok(JsonNodeFactory.instance.pojoNode(entryPointService.updateData(slug, id, body)));
	}

	@PostMapping
	public ResponseEntity<JsonNode> entryPost(@PathVariable String slug, @RequestBody(required = false) JsonNode body) {
		return ResponseEntity.ok(JsonNodeFactory.instance.pojoNode(entryPointService.createData(slug, body)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<JsonNode> entryDelete(ServerHttpRequest request, @PathVariable String slug, @PathVariable String id) {
		return ResponseEntity.ok(JsonNodeFactory.instance.pojoNode(entryPointService.deleteData(slug, id)));
	}

}
