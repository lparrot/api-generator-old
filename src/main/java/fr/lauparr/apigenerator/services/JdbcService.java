package fr.lauparr.apigenerator.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.lauparr.apigenerator.entities.Content;
import fr.lauparr.apigenerator.entities.ContentField;
import fr.lauparr.apigenerator.pojo.dto.PaginationDTO;
import fr.lauparr.apigenerator.pojo.dto.RelationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static fr.lauparr.apigenerator.utils.StringUtils.toSnakeCase;

@Slf4j
@Service
public class JdbcService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private DataSource dataSource;

	@Transactional
	public void createTable(Content content) {
		try {
			String tableName = content.getTableName();

			// Si la table existe déjà, on quitte le service
			if (checkIfTableExists(tableName)) {
				return;
			}

			createTableIfNotExists(tableName, content.getContentFields().stream().filter(ContentField::isPrimaryKey).findFirst().orElseThrow(() -> new IllegalArgumentException("No primary key defined in content")));

			List<ContentField> allFieldWithoutPrimaryKey = content.getContentFields().stream().filter(contentField -> !contentField.isPrimaryKey()).collect(Collectors.toList());
			for (ContentField contentField : allFieldWithoutPrimaryKey) {
				createTableField(tableName, toSnakeCase(contentField.getName()), contentField.getDatabaseTypeWithLength(), contentField.isNullable());
			}

		} catch (SQLException e) {
			log.error("Erreur lors de la création de la table", e);
		}
	}

	@Transactional(readOnly = true)
	public boolean checkIfTableExists(String tableName) throws SQLException {
		ResultSet resultSet;

		try (Connection connection = dataSource.getConnection()) {
			final DatabaseMetaData metaData = connection.getMetaData();
			resultSet = metaData.getTables(connection.getCatalog(), null, tableName, new String[]{"TABLE"});
		}

		return resultSet.next();
	}

	@Transactional
	public void createTableIfNotExists(String tableName, ContentField primaryKeyField) {
		jdbcTemplate.update(String.format("create table if not exists %s (%s)", tableName, String.format("%s %s not null primary key", primaryKeyField.getDbFieldName(), primaryKeyField.getDatabaseTypeWithLength())));
	}

	@Transactional
	public void updateTable(String oldTableName, String newTableName) {
		jdbcTemplate.update(String.format("alter table %s rename to %s", oldTableName, newTableName));
	}

	@Transactional
	public void deleteTable(String tableName) {
		jdbcTemplate.update(String.format("drop table if exists %s", tableName));
	}

	@Transactional
	public void createTableField(String tableName, String fieldName, String fieldType, boolean nullable) {
		jdbcTemplate.update(String.format("alter table %s add %s %s %s", tableName, fieldName, fieldType, nullable ? "" : " not null"));
	}

	@Transactional
	public void updateTableField(String tableName, String oldFieldName, String newFieldName, String fieldType, boolean nullable) {
		jdbcTemplate.update(String.format("alter table %s change column %s %s %s %s", tableName, oldFieldName, newFieldName, fieldType, nullable ? "" : " not null"));
	}

	@Transactional(readOnly = true)
	public PaginationDTO findData(String tableName, String[] fieldNames, Pageable pageable, final List<RelationDTO> relations) {
		StringBuilder pageQuery = new StringBuilder();

		// Pagination
		if (pageable.getSort().isSorted()) {
			Sort.Order order = pageable.getSort().toList().get(0);
			pageQuery.append(String.format(" order by %s %s", order.getProperty(), order.getDirection()));
		}

		int page = Math.max(pageable.getPageNumber() - 1, 0);
		pageQuery.append(String.format(" limit %s offset %s", pageable.getPageSize(), page * pageable.getPageSize()));

		// Query
		List<Object> objects = jdbcTemplate
			.queryForList(String.format("select %s from %s %s", String.join(",", fieldNames), tableName, pageQuery))
			.stream().map(map -> objectMapper.convertValue(map, Object.class))
			.collect(Collectors.toList());

		Integer total = jdbcTemplate.queryForObject(String.format("select count(*) from %s", tableName), Integer.class);

		List<ObjectNode> nodes = objects.stream()
			.map(data -> objectMapper.convertValue(data, ObjectNode.class))
			.peek(node -> {
				relations.forEach(relation -> {
					Object data;
					try {
						data = jdbcTemplate.queryForObject(String.format("select * from %s where id = ?", relation.getTargetedTable()), getObjectRowMapper(), node.get(relation.getField()).asText());
					} catch (Exception e) {
						data = null;
					}

					if (data != null) {
						node.set(relation.getField(), JsonNodeFactory.instance.pojoNode(data));
					}
				});
			})
			.collect(Collectors.toList());

		return PaginationDTO.builder()
			.list(objectMapper.convertValue(nodes, new TypeReference<List<Object>>() {}))
			.page(pageable.getPageNumber())
			.size(pageable.getPageSize())
			.total(total)
			.build();
	}

	@Transactional(readOnly = true)
	public List<Object> findData(String tableName, String[] fieldNames) {
		return jdbcTemplate
			.queryForList(String.format("select %s from %s", String.join(",", fieldNames), tableName))
			.stream().map(map -> objectMapper.convertValue(map, Object.class))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Object findDataById(String tableName, String[] fieldNames, Object id) {
		RowMapper<Object> rowMapper = getObjectRowMapper();

		try {
			return jdbcTemplate.queryForObject(String.format("select %s from %s where id = ?", String.join(",", fieldNames), tableName), rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private RowMapper<Object> getObjectRowMapper() {
		return (resultSet, i) -> {
			ObjectNode node = JsonNodeFactory.instance.objectNode();
			final ResultSetMetaData metaData = resultSet.getMetaData();
			IntStream.range(0, metaData.getColumnCount())
				.forEach(index -> {
					try {
						node.set(metaData.getColumnName(index), JsonNodeFactory.instance.pojoNode(resultSet.getObject(index)));
					} catch (Exception e) {
						//
					}
				});

			return objectMapper.convertValue(node, Object.class);
		};
	}

	@Transactional
	public Object updateDataById(String tableName, String[] fieldNames, String id, JsonNode body) {
		List<String> conditions = new ArrayList<>();
		List<Object> values = new ArrayList<>();

		Map<String, Object> result = objectMapper.convertValue(body, new TypeReference<Map<String, Object>>() {});

		result.keySet().forEach(fieldName -> {
			conditions.add(fieldName + " = ?");
			values.add(result.get(fieldName));
		});

		values.add(id);

		jdbcTemplate.update(String.format("update %s set %s where id = ?", tableName, String.join(",", conditions)), values.toArray(new Object[0]));
		return findDataById(tableName, fieldNames, id);
	}

	@Transactional
	public Object createData(String tableName, String[] fieldNames, JsonNode body) {
		Map<String, Object> map = objectMapper.convertValue(body, new TypeReference<Map<String, Object>>() {});

		Object id = map.get("id");

		if (id == null) {
			id = UUID.randomUUID().toString();
			map.put("id", id);
		}

		String parameters = IntStream.range(0, map.size())
			.mapToObj(i -> "?")
			.collect(Collectors.joining(","));

		jdbcTemplate.update(String.format("insert into %s (%s) values (%s)",
				tableName,
				String.join(",", map.keySet()), parameters),
			map.values().toArray(new Object[]{}));

		return findDataById(tableName, fieldNames, id);
	}

	@Transactional
	public boolean deleteDataById(String tableName, String id) {
		int updateCount = jdbcTemplate.update(String.format("delete from %s where id = ?", tableName), id);
		return updateCount > 0;
	}
}
