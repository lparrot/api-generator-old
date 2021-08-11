package fr.lauparr.apigenerator.controllers;

import fr.lauparr.apigenerator.enums.EnumContentFieldRelationType;
import fr.lauparr.apigenerator.enums.EnumContentFieldType;
import fr.lauparr.apigenerator.services.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ListController {

	@Autowired
	private ListService listService;

	@GetMapping("/field_types")
	public List<EnumContentFieldType> findAllFieldTypes() {
		return this.listService.getAllFieldTypes();
	}

	@GetMapping("/field_relation_types")
	public List<EnumContentFieldRelationType> findAllFieldRelationTypes() {
		return this.listService.getAllFieldRelationTypes();
	}

}
