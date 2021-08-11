package fr.lauparr.apigenerator.pojo.vm;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
	private boolean hideInList;
	private boolean nullable;
	private ObjectNode params;

	@Builder
	public ContentFieldVM(String name, EnumContentFieldType type, boolean hideInList, boolean nullable, ObjectNode params) {
		this.name = name;
		this.type = type;
		this.hideInList = hideInList;
		this.nullable = nullable;
		this.params = params;
	}
}
