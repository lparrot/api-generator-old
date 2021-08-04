package fr.lauparr.apigenerator.services;

import fr.lauparr.apigenerator.entities.Content;
import fr.lauparr.apigenerator.entities.ContentField;
import fr.lauparr.apigenerator.enums.EnumContentFieldType;
import fr.lauparr.apigenerator.pojo.dto.ContentFieldSimpleDTO;
import fr.lauparr.apigenerator.pojo.dto.ContentSimpleDTO;
import fr.lauparr.apigenerator.pojo.mapper.ContentFieldMapper;
import fr.lauparr.apigenerator.pojo.mapper.ContentMapper;
import fr.lauparr.apigenerator.pojo.vm.ContentFieldVM;
import fr.lauparr.apigenerator.repositories.ContentFieldRepository;
import fr.lauparr.apigenerator.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
		return contentMapper.entityToDto(contentRepository.findById(id).orElseThrow(EntityNotFoundException::new));
	}

	@Transactional
	public ContentSimpleDTO createContent(Content content) {
		if (Arrays.stream(content.getFieldNames()).noneMatch("id"::equals)) {
			content.addField(ContentField.builder().name("Id").nullable(false).contentType(EnumContentFieldType.STRING).build());
		}

		contentRepository.save(content);
		return contentMapper.entityToDto(content);
	}

	@Transactional
	public ContentFieldSimpleDTO updateField(Long idContent, Long idField, ContentFieldVM body) {
		ContentField contentField = contentFieldRepository.findById(idField).orElseThrow(EntityNotFoundException::new);

		String oldFieldname = contentField.getDbFieldName();

		if (contentField.getContent() != null && !idContent.equals(contentField.getContent().getId())) {
			throw new EntityNotFoundException();
		}

		contentFieldMapper.updateEntityFromVm(body, contentField);
		contentField = contentFieldRepository.save(contentField);

		jdbcService.updateField(contentField.getContent().getTableName(), oldFieldname, contentField.getDbFieldName(), contentField.getDatabaseTypeWithLength(), contentField.isNullable());

		return contentFieldMapper.entityToDto(contentField);
	}

	@Transactional
	public ContentFieldSimpleDTO addField(Long idContent, ContentFieldVM body) {
		Content content = contentRepository.findById(idContent).orElseThrow(EntityNotFoundException::new);
		ContentField contentField = new ContentField();
		contentFieldMapper.updateEntityFromVm(body, contentField);
		contentField.setContent(contentRepository.getOne(idContent));
		contentField = contentFieldRepository.save(contentField);

		jdbcService.createField(content.getTableName(), contentField.getDbFieldName(), contentField.getDatabaseTypeWithLength(), contentField.isNullable());

		return contentFieldMapper.entityToDto(contentField);
	}

}
