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
@RequestMapping("/api/{slug}")
public class EntryPointController {

	@Autowired
	private EntryPointService entryPointService;

	@GetMapping("/{*path}")
	public ResponseEntity<JsonNode> entryGet(ServerHttpRequest request, @PathVariable String slug, @PathVariable String path, Pageable page) {
		return ResponseEntity.ok(JsonNodeFactory.instance.pojoNode(entryPointService.getData(slug, page)));
	}

	@GetMapping("/{id}/{*path}")
	public ResponseEntity<JsonNode> entryGetId(ServerHttpRequest request, @PathVariable String slug, @PathVariable String id, @PathVariable String path) {
		return ResponseEntity.ok(JsonNodeFactory.instance.pojoNode(entryPointService.getDataById(slug, id)));
	}

	@PutMapping("/{id}/{*path}")
	public ResponseEntity<JsonNode> entryPut(ServerHttpRequest request, @PathVariable String slug, @PathVariable String id, @PathVariable String path, @RequestBody(required = false) JsonNode body) {
		return ResponseEntity.ok(JsonNodeFactory.instance.pojoNode(entryPointService.updateData(slug, id, body)));
	}

	@PostMapping("/{*path}")
	public ResponseEntity<JsonNode> entryPost(ServerHttpRequest request, @PathVariable String slug, @PathVariable String path, @RequestBody(required = false) JsonNode body) {
		return ResponseEntity.ok(JsonNodeFactory.instance.pojoNode(entryPointService.createData(slug, body)));
	}

	@DeleteMapping("/{id}/{*path}")
	public ResponseEntity<JsonNode> entryDelete(ServerHttpRequest request, @PathVariable String slug, @PathVariable String id, @PathVariable String path) {
		return ResponseEntity.ok(JsonNodeFactory.instance.pojoNode(entryPointService.deleteData(slug, id)));
	}

}
