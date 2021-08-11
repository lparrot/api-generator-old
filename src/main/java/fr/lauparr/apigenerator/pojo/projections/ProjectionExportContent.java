package fr.lauparr.apigenerator.pojo.projections;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface ProjectionExportContent {
	String getName();

	List<ProjectionExportContentContentField> getContentFields();

	interface ProjectionExportContentContentField {
		String getName();

		@Value("#{target.type.name()}")
		String getType();

		boolean isNullable();

		boolean isHideInList();

		boolean isPrimaryKey();

		ObjectNode getParams();
	}
}
