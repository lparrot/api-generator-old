package fr.lauparr.apigenerator.pojo.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.lauparr.apigenerator.enums.EnumContentFieldRelationType;
import fr.lauparr.apigenerator.enums.EnumContentFieldType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentFieldSimpleDTO {
	private Long id;
	private String name;
	private String dbFieldName;
	private boolean nullable;
	private boolean primaryKey;
	private ObjectNode params;
	private EnumContentFieldType type;

	private EnumContentFieldRelationType relationType;
	private ContentSimpleDTO relationContent;
}
