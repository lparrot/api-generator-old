package fr.lauparr.apigenerator.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EnumContentFieldShowType {

	PROPERTY("Property"), EXPRESSION("Expression");

	@Getter
	private final String label;

	EnumContentFieldShowType(String label) {
		this.label = label;
	}

	public String getCode() {
		return this.name();
	}

}
