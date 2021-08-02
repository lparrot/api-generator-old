package fr.lauparr.apigenerator.pojo.dto;

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
	private boolean hideInList;
	private EnumContentFieldType type;
}
