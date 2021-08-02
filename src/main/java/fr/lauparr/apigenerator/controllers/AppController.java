package fr.lauparr.apigenerator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

@RestController
@RequestMapping("/app")
public class AppController {

	@Autowired
	RequestMappingHandlerMapping mapping;

//	@PostMapping("/endpoint")
//	public ResponseEntity<?> addEndpoint(@RequestBody AddEndpointVM model) throws NoSuchMethodException {
//		Method method = EntryPointController.class.getMethod("entryPost", String.class, String.class, JsonNode.class);
//
//		RequestMappingInfo info = RequestMappingInfo.paths(model.getUrl())
//			.methods(RequestMethod.POST)
//			.build();
//
//		this.mapping.registerMapping(info, "entryPointController", method);
//
//		return ResponseEntity.ok(model);
//	}

}
