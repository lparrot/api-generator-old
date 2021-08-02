package fr.lauparr.apigenerator.services;

import com.fasterxml.jackson.databind.JsonNode;
import fr.lauparr.apigenerator.entities.Content;
import fr.lauparr.apigenerator.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryPointService {

	@Autowired
	private JdbcService jdbcService;
	@Autowired
	private ContentRepository contentRepository;

	public List<Object> getContent(String slug) {
		Content content = getContentBySlug(slug);
		return jdbcService.findData(content.getTableName(), content.getFieldNames());
	}

	public Object getContentById(String slug, String id) {
		Content content = getContentBySlug(slug);
		return jdbcService.findDataById(content.getTableName(), content.getFieldNames(), id);
	}

	public Object updateContent(String slug, String id, JsonNode body) {
		Content content = getContentBySlug(slug);
		return jdbcService.updateById(content.getTableName(), content.getFieldNamesWithoutPrimaryKey(), id, body);
	}

	public Object createContent(String slug, JsonNode body) {
		Content content = getContentBySlug(slug);
		return jdbcService.create(content.getTableName(), content.getFieldNamesWithoutPrimaryKey(), body);
	}

	public Object deleteContent(String slug, String id) {
		Content content = getContentBySlug(slug);
		return jdbcService.deleteById(content.getTableName(), id);
	}

	private Content getContentBySlug(String slug) {
		return contentRepository.findBySlugEqualsIgnoreCase(slug);
	}
}
