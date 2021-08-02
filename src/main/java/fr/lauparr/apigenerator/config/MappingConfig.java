package fr.lauparr.apigenerator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class MappingConfig {

	@Autowired
	public void setHandlerMapping(RequestMappingHandlerMapping mapping) throws NoSuchMethodException {
		// Enregistrement méthode GET
//		Method getMethod = EntryPointController.class.getMethod("entryGet", String.class, String.class);
//
//		RequestMappingInfo getMapping = RequestMappingInfo.paths("/{nameSlug}/api/{version}/users/{id}")
//			.methods(RequestMethod.GET)
//			.build();
//
//		mapping.registerMapping(getMapping, "entryPointController", getMethod);

		// Enregistrement méthode POST
//		Method postMethod = EntryPointController.class.getMethod("entryPost", String.class, String.class, JsonNode.class);
//
//		RequestMappingInfo postMapping = RequestMappingInfo.paths("/{nameSlug}/api/{version}")
//			.methods(RequestMethod.POST)
//			.build();
//
//		mapping.registerMapping(postMapping, "entryPointController", postMethod);
	}

}
