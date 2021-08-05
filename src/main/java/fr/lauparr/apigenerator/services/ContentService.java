package fr.lauparr.apigenerator.services;

import fr.lauparr.apigenerator.entities.Content;
import fr.lauparr.apigenerator.entities.ContentField;
import fr.lauparr.apigenerator.enums.EnumContentFieldType;
import fr.lauparr.apigenerator.exceptions.DataNotFoundException;
import fr.lauparr.apigenerator.pojo.dto.ContentFieldSimpleDTO;
import fr.lauparr.apigenerator.pojo.dto.ContentSimpleDTO;
import fr.lauparr.apigenerator.pojo.mapper.ContentFieldMapper;
import fr.lauparr.apigenerator.pojo.mapper.ContentMapper;
import fr.lauparr.apigenerator.pojo.vm.ContentFieldVM;
import fr.lauparr.apigenerator.pojo.vm.ContentVM;
import fr.lauparr.apigenerator.repositories.ContentFieldRepository;
import fr.lauparr.apigenerator.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ContentService {

	@Autowired
	private ContentRepository contentRepository;
	@Autowired
	private ContentFieldRepository contentFieldRepository;
	@Autowired
	private ContentMapper contentMapper;
	@Autowired
	private ContentFieldMapper contentFieldMapper;
	@Autowired
	private JdbcService jdbcService;

	@Transactional(readOnly = true)
	public List<ContentSimpleDTO> getContents() {
		return contentMapper.entitiesToDtos(contentRepository.findAll(Sort.by(Sort.Order.asc("name"))));
	}

	@Transactional(readOnly = true)
	public ContentSimpleDTO getContentById(Long id) {
		return contentMapper.entityToDto(contentRepository.findById(id).orElseThrow(DataNotFoundException::new));
	}

	@Transactional
	public ContentSimpleDTO createContent(ContentVM body) {
		Content content = new Content();
		contentMapper.updateEntityFromVm(body, content);
		return createContent(content);
	}

	@Transactional
	public ContentSimpleDTO createContent(Content content) {
		if (Arrays.stream(content.getFieldNames()).noneMatch("id"::equals)) {
			content.addField(ContentField.builder().name("Id").nullable(false).primaryKey(true).hideInList(true).contentType(EnumContentFieldType.STRING).build());
		}

		content = contentRepository.save(content);

		jdbcService.createTable(content);

		return contentMapper.entityToDto(content);
	}

	@Transactional
	public ContentSimpleDTO updateContent(Long id, ContentVM body) {
		Content content = contentRepository.findById(id).orElseThrow(DataNotFoundException::new);

		String oldTableName = content.getTableName();

		contentMapper.updateEntityFromVm(body, content);

		if (!oldTableName.equals(content.getTableName())) {
			jdbcService.updateTable(oldTableName, content.getTableName());
		}

		content = contentRepository.save(content);

		return contentMapper.entityToDto(content);
	}

	@Transactional
	public void deleteContent(Long id) {
		Content content = contentRepository.findById(id).orElseThrow(DataNotFoundException::new);

		jdbcService.deleteTable(content.getTableName());

		contentRepository.deleteById(id);
	}

	@Transactional
	public ContentFieldSimpleDTO updateField(Long idContent, Long idField, ContentFieldVM body) {
		ContentField contentField = contentFieldRepository.findById(idField).orElseThrow(DataNotFoundException::new);

		String oldFieldname = contentField.getDbFieldName();

		if (contentField.getContent() != null && !idContent.equals(contentField.getContent().getId())) {
			throw new DataNotFoundException();
		}

		contentFieldMapper.updateEntityFromVm(body, contentField);
		contentField = contentFieldRepository.save(contentField);

		jdbcService.updateTableField(contentField.getContent().getTableName(), oldFieldname, contentField.getDbFieldName(), contentField.getDatabaseTypeWithLength(), contentField.isNullable());

		return contentFieldMapper.entityToDto(contentField);
	}

	@Transactional
	public ContentFieldSimpleDTO addField(Long idContent, ContentFieldVM body) {
		Content content = contentRepository.findById(idContent).orElseThrow(DataNotFoundException::new);
		ContentField contentField = new ContentField();
		contentFieldMapper.updateEntityFromVm(body, contentField);
		contentField.setContent(contentRepository.getOne(idContent));
		contentField = contentFieldRepository.save(contentField);

		jdbcService.createTableField(content.getTableName(), contentField.getDbFieldName(), contentField.getDatabaseTypeWithLength(), contentField.isNullable());

		return contentFieldMapper.entityToDto(contentField);
	}
}
