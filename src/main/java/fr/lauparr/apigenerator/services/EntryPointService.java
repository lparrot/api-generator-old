package fr.lauparr.apigenerator.services;

import com.fasterxml.jackson.databind.JsonNode;
import fr.lauparr.apigenerator.entities.Content;
import fr.lauparr.apigenerator.exceptions.DataNotFoundException;
import fr.lauparr.apigenerator.pojo.dto.PaginationDTO;
import fr.lauparr.apigenerator.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EntryPointService {

	@Autowired
	private JdbcService jdbcService;
	@Autowired
	private ContentRepository contentRepository;

	public PaginationDTO getData(String slug, Pageable page) {
		Content content = getContentBySlug(slug);
		return jdbcService.findData(content.getTableName(), content.getFieldNames(), page);
	}

	public Object getDataById(String slug, String id) {
		Content content = getContentBySlug(slug);
		return jdbcService.findDataById(content.getTableName(), content.getFieldNames(), id);
	}

	public Object updateData(String slug, String id, JsonNode body) {
		Content content = getContentBySlug(slug);
		return jdbcService.updateDataById(content.getTableName(), content.getFieldNamesWithoutPrimaryKey(), id, body);
	}

	public Object createData(String slug, JsonNode body) {
		Content content = getContentBySlug(slug);
		return jdbcService.createData(content.getTableName(), content.getFieldNamesWithoutPrimaryKey(), body);
	}

	public Object deleteData(String slug, String id) {
		Content content = getContentBySlug(slug);
		return jdbcService.deleteDataById(content.getTableName(), id);
	}

	private Content getContentBySlug(String slug) {
		return Optional.ofNullable(contentRepository.findBySlugEqualsIgnoreCase(slug)).orElseThrow(DataNotFoundException::new);
	}
}
