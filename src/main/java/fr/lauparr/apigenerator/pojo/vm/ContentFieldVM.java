package fr.lauparr.apigenerator.pojo.vm;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.lauparr.apigenerator.enums.EnumContentFieldRelationType;
import fr.lauparr.apigenerator.enums.EnumContentFieldType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContentFieldVM {
	private String name;
	private EnumContentFieldType type;
	private boolean nullable;
	private Integer length;
	private Long relationContentId;
	private EnumContentFieldRelationType relationType;
	private ObjectNode params;

	@Builder
	public ContentFieldVM(String name, EnumContentFieldType type, boolean nullable, Long relationContentId, EnumContentFieldRelationType relationType, ObjectNode params) {
		this.name = name;
		this.type = type;
		this.nullable = nullable;
		this.relationContentId = relationContentId;
		this.relationType = relationType;
		this.params = params;
	}
}
