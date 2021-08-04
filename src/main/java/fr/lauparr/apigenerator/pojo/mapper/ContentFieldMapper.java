package fr.lauparr.apigenerator.pojo.mapper;

import fr.lauparr.apigenerator.entities.ContentField;
import fr.lauparr.apigenerator.pojo.dto.ContentFieldSimpleDTO;
import fr.lauparr.apigenerator.pojo.vm.ContentFieldVM;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ContentFieldMapper {

	public abstract void updateEntityFromVm(ContentFieldVM vm, @MappingTarget ContentField entity);

	public abstract ContentFieldSimpleDTO entityToDto(ContentField entity);

	public abstract List<ContentFieldSimpleDTO> entitiesToDtos(List<ContentField> entities);

}
