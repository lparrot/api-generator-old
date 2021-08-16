package fr.lauparr.apigenerator.pojo.vm;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContentVM {
	private String name;
	private ArrayNode contentShowFields;

	@Builder
	public ContentVM(String name, ArrayNode contentShowFields) {
		this.name = name;
		this.contentShowFields = contentShowFields;
	}
}
