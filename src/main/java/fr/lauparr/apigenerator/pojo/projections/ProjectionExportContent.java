package fr.lauparr.apigenerator.pojo.projections;

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
	}
}
