package fr.lauparr.apigenerator.pojo.mapper;

import fr.lauparr.apigenerator.entities.ContentField;
import fr.lauparr.apigenerator.pojo.dto.ContentFieldSimpleDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ContentFieldMapper {

	public abstract ContentFieldSimpleDTO entityToDto(ContentField entity);

	public abstract List<ContentFieldSimpleDTO> entitiesToDtos(List<ContentField> entities);

}
