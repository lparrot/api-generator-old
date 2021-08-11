package fr.lauparr.apigenerator.pojo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelationDTO {
	private String field;
	private String targetedTable;
	private String[] fieldNames;
}
