package fr.lauparr.apigenerator.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.slugify.Slugify;
import fr.lauparr.apigenerator.enums.EnumContentFieldType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static fr.lauparr.apigenerator.utils.StringUtils.toSnakeCase;

@Table(name = "content")
@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Content {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "slug")
	private String slug;

	@Column(name = "displayed_field")
	private String displayedField;

	@Column(name = "content_show_fields", columnDefinition = "TEXT")
	private ArrayNode contentShowFields;

	@OrderBy("id")
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "content", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
	private List<ContentField> contentFields = new ArrayList<>();

	@Builder
	public Content(String name) {
		this.name = name;
	}

	public void addField(ContentField field) {
		field.setContent(this);
		contentFields.add(field);
	}

	public String[] getFieldNames() {
		return this.contentFields.stream().map(ContentField::getDbFieldName).toArray(String[]::new);
	}

	public String[] getFieldNamesWithoutRelation() {
		return this.contentFields.stream().filter(contentField -> !contentField.getType().equals(EnumContentFieldType.RELATION)).map(ContentField::getDbFieldName).toArray(String[]::new);
	}

	public String[] getFieldNamesWithoutPrimaryKey() {
		return this.contentFields.stream().filter(contentField -> !contentField.isPrimaryKey()).map(ContentField::getDbFieldName).toArray(String[]::new);
	}

	public String getTableName() {
		return toSnakeCase(this.getName());
	}

	@PreUpdate
	public void preUpdate() {
		slugify();
	}

	@PrePersist
	public void prePersist() {
		slugify();
	}

	private void slugify() {
		Slugify slugify = new Slugify();
		slug = slugify.slugify(name);
	}
}

