package fr.lauparr.apigenerator.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.lauparr.apigenerator.enums.EnumContentFieldRelationType;
import fr.lauparr.apigenerator.enums.EnumContentFieldType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static fr.lauparr.apigenerator.utils.StringUtils.toSnakeCase;

@Table(name = "content_field")
@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ContentField {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "length")
	private String length;

	@Column(name = "primary_key")
	private boolean primaryKey;

	@Column(name = "nullable")
	private boolean nullable;

	@Column(name = "params")
	private ObjectNode params;

	@Enumerated(EnumType.STRING)
	private EnumContentFieldType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id")
	private Content content;

	@Column(name = "relation_type")
	@Enumerated(EnumType.STRING)
	private EnumContentFieldRelationType relationType;

	@ManyToOne
	@JoinColumn(name = "relation_content_id")
	private Content relationContent;

	@Builder
	public ContentField(String name, boolean nullable, boolean primaryKey, ObjectNode params, EnumContentFieldType contentType, EnumContentFieldRelationType relationType, Content relationContent, Content content) {
		this.name = name;
		this.nullable = nullable;
		this.primaryKey = primaryKey;
		this.params = params;
		this.type = contentType;
		this.relationType = relationType;
		this.relationContent = relationContent;

		this.content = content;
	}

	public String getDbFieldName() {
		return toSnakeCase(this.getName());
	}

	public String getDatabaseTypeWithLength() {
		if (length != null) {
			return String.format("%s(%s)", type.getDatabaseType(), length);
		}
		if (type.getDefaultLength() != null) {
			return String.format("%s(%s)", type.getDatabaseType(), type.getDefaultLength());
		}
		return type.getDatabaseType();
	}
}
