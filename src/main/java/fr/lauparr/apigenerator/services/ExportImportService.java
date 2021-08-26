package fr.lauparr.apigenerator.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.lauparr.apigenerator.entities.Content;
import fr.lauparr.apigenerator.pojo.dto.ContentSimpleDTO;
import fr.lauparr.apigenerator.pojo.projections.ProjectionExportContent;
import fr.lauparr.apigenerator.pojo.vm.ContentFieldVM;
import fr.lauparr.apigenerator.pojo.vm.ContentVM;
import fr.lauparr.apigenerator.repositories.ContentRepository;
import fr.lauparr.apigenerator.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ExportImportService {

	@Autowired
	private ContentRepository contentRepository;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ContentService contentService;
	@Autowired
	private JdbcService jdbcService;

	@Transactional(readOnly = true)
	public ObjectNode exportContents(boolean full) {
		ObjectNode rootNode = JsonNodeFactory.instance.objectNode();

		List<Content> contents = contentRepository.findAll();

		rootNode.set("structure", objectMapper.valueToTree(DaoUtils.convertListDto(contents, ProjectionExportContent.class)));

		if (full) {
			ObjectNode dataNode = JsonNodeFactory.instance.objectNode();

			contents.forEach(content -> {
				dataNode.set(content.getSlug(), objectMapper.valueToTree(jdbcService.findData(content.getTableName(), content.getFieldNames())));
			});
			rootNode.set("data", dataNode);
		}
		return rootNode;
	}

	public void importContents(MultipartFile file) throws IOException {
		if (!"application/json".equals(file.getContentType())) {
			throw new IllegalArgumentException("Le seul type de fichier accepté est un fichier de type JSON.");
		}

		ObjectNode node;

		try {
			node = objectMapper.readValue(file.getBytes(), ObjectNode.class);
		} catch (Exception e) {
			throw new IllegalArgumentException("Erreur lors de la lecture du fichier.");
		}

		if (!node.hasNonNull("structure")) {
			throw new IllegalArgumentException("Le fichier doit avoir au moins un attribut nommé 'structure' contenant la liste des champs à importer");
		}

		// Import structure fields

		List<ContentSimpleDTO> structure = objectMapper.convertValue(node.path("structure"), new TypeReference<List<ContentSimpleDTO>>() {});

		structure.forEach(contentDto -> {
			ContentSimpleDTO content = contentService.createContent(ContentVM.builder().name(contentDto.getName()).contentShowFields(contentDto.getContentShowFields()).build());

			contentDto.getContentFields().forEach(contentFieldDto -> {
				contentService.addField(content.getId(), ContentFieldVM.builder()
					.name(contentFieldDto.getName())
					.type(contentFieldDto.getType())
					.nullable(contentFieldDto.isNullable())
					.hideInList(contentFieldDto.isHideInList())
					.params(contentFieldDto.getParams())
					.build());
			});
		});

		// Import data

		if (node.hasNonNull("data")) {
			node.path("data").fields().forEachRemaining(entry -> {
				Content content = contentRepository.findBySlugEqualsIgnoreCase(entry.getKey());

				if (content != null) {
					ArrayNode entryArray = (ArrayNode) entry.getValue();
					entryArray.elements().forEachRemaining(jsonNode -> {
						jdbcService.createData(content.getTableName(), content.getFieldNames(), jsonNode);
					});
				}
			});
		}
	}
}
