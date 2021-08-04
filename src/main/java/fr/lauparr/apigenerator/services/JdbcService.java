package fr.lauparr.apigenerator.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lauparr.apigenerator.entities.Content;
import fr.lauparr.apigenerator.entities.ContentField;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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

	public boolean checkIfTableExists(String tableName) throws SQLException {
		ResultSet resultSet;

		try (Connection connection = dataSource.getConnection()) {
			final DatabaseMetaData metaData = connection.getMetaData();
			resultSet = metaData.getTables(connection.getCatalog(), null, tableName, new String[]{"TABLE"});
		}

		return resultSet.next();
	}

	public void createTableIfNotExists(String tableName, ContentField primaryKeyField) {
		jdbcTemplate.update(String.format("create table if not exists %s (%s)", tableName, String.format("%s %s not null primary key", primaryKeyField.getDbFieldName(), primaryKeyField.getDatabaseTypeWithLength())));
	}

	public void updateTable(String oldTableName, String newTableName) {
		jdbcTemplate.update(String.format("alter table %s rename to %s", oldTableName, newTableName));
	}

	public void deleteTable(String tableName) {
		jdbcTemplate.update(String.format("drop table if exists %s", tableName));
	}

	public void createTableField(String tableName, String fieldName, String fieldType, boolean nullable) {
		jdbcTemplate.update(String.format("alter table %s add %s %s %s", tableName, fieldName, fieldType, nullable ? "" : " not null"));
	}

	public void updateTableField(String tableName, String oldFieldName, String newFieldName, String fieldType, boolean nullable) {
		jdbcTemplate.update(String.format("alter table %s change column %s %s %s %s", tableName, oldFieldName, newFieldName, fieldType, nullable ? "" : " not null"));
	}

	public List<Object> findData(String tableName, String[] fieldNames, Pageable pageable) {
		StringBuilder pageQuery = new StringBuilder();

		// Création de la pagination
		if (pageable != null && pageable.isPaged()) {
			if (pageable.getSort().isSorted()) {
				Sort.Order order = pageable.getSort().toList().get(0);
				pageQuery.append(String.format(" order by %s %s", order.getProperty(), order.getDirection()));
			}

			int page = Math.max(pageable.getPageNumber() - 1, 0);
			pageQuery.append(String.format(" limit %s offset %s", pageable.getPageSize(), page * pageable.getPageSize()));
		}

		System.out.println(String.format("select %s from %s %s", String.join(",", fieldNames), tableName, pageQuery));

		return jdbcTemplate
			.queryForList(String.format("select %s from %s %s", String.join(",", fieldNames), tableName, pageQuery))
			.stream().map(map -> objectMapper.convertValue(map, Object.class))
			.collect(Collectors.toList());
	}

	public Object findDataById(String tableName, String[] fieldNames, String id) {
		RowMapper<Object> rowMapper = (resultSet, i) -> {
			Map<String, Object> map = new HashMap<>();
			for (String fieldName : fieldNames) {
				map.put(fieldName, resultSet.getObject(fieldName));
			}
			return objectMapper.convertValue(map, Object.class);
		};

		return jdbcTemplate.queryForObject(String.format("select %s from %s where id = ?", String.join(",", fieldNames), tableName), rowMapper, id);
	}

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

	public Object createData(String tableName, String[] fieldNames, JsonNode body) {
		String catalog;
		try {
			catalog = dataSource.getConnection().getCatalog();
		} catch (SQLException e) {
			throw new GenericJDBCException("Erreur lors de la récupération du schema de base de données", e);
		}
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withCatalogName(catalog).withTableName(tableName);
		Map<String, Object> map = objectMapper.convertValue(body, new TypeReference<Map<String, Object>>() {});
		String id = UUID.randomUUID().toString();
		map.put("id", id);
		simpleJdbcInsert.execute(map);
		return findDataById(tableName, fieldNames, id);
	}

	public boolean deleteDataById(String tableName, String id) {
		int updateCount = jdbcTemplate.update(String.format("delete from %s where id = ?", tableName), id);
		return updateCount > 0;
	}
}
