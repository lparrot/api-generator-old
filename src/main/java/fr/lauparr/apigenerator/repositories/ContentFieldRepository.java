package fr.lauparr.apigenerator.repositories;

import fr.lauparr.apigenerator.entities.ContentField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContentFieldRepository extends JpaRepository<ContentField, Long>, JpaSpecificationExecutor<ContentField> {
}
