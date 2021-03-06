package fr.lauparr.apigenerator.services;

import fr.lauparr.apigenerator.enums.EnumContentFieldRelationType;
import fr.lauparr.apigenerator.enums.EnumContentFieldShowType;
import fr.lauparr.apigenerator.enums.EnumContentFieldType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ListService {

	public List<EnumContentFieldType> getAllFieldTypes() {
		return Arrays.asList(EnumContentFieldType.values());
	}

	public List<EnumContentFieldRelationType> getAllFieldRelationTypes() {
		return Arrays.asList(EnumContentFieldRelationType.values());
	}

	public List<EnumContentFieldShowType> getAllFieldShowTypes() {
		return Arrays.asList(EnumContentFieldShowType.values());
	}
}
