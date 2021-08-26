package fr.lauparr.apigenerator.pojo.dto;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ContentSimpleDTO {
	private Long id;
	private String name;
	private String slug;
	private String displayedField;
	private ArrayNode contentShowFields;
	private List<ContentFieldSimpleDTO> contentFields;
}
