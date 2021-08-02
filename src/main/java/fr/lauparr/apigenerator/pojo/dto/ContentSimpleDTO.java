package fr.lauparr.apigenerator.pojo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ContentSimpleDTO {
	private Long id;
	private String name;
	private String slug;
	private List<ContentFieldSimpleDTO> contentFields;
}
