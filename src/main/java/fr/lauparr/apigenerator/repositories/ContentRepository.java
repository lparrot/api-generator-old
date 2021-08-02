package fr.lauparr.apigenerator.repositories;

import fr.lauparr.apigenerator.entities.Content;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContentRepository extends JpaRepository<Content, Long>, JpaSpecificationExecutor<Content> {
	@EntityGraph(attributePaths = {"contentFields"})
	Content findBySlugEqualsIgnoreCase(String slug);
}
