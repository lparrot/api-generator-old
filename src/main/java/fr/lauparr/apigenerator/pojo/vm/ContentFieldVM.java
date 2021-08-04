package fr.lauparr.apigenerator.pojo.vm;

import fr.lauparr.apigenerator.enums.EnumContentFieldType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentFieldVM {
	private String name;
	private EnumContentFieldType type;
	private boolean hideInList;
	private boolean nullable;
}
