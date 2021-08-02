package fr.lauparr.apigenerator.pojo.mapper;

import fr.lauparr.apigenerator.entities.Content;
import fr.lauparr.apigenerator.pojo.dto.ContentSimpleDTO;
import fr.lauparr.apigenerator.pojo.vm.ContentVM;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ContentFieldMapper.class})
public abstract class ContentMapper {

	public abstract void updateEntityFromVm(ContentVM vm, @MappingTarget Content entity);

	public abstract ContentSimpleDTO entityToDto(Content entity);

	public abstract List<ContentSimpleDTO> entitiesToDtos(List<Content> entities);

	public abstract Content vmToEntity(ContentVM vm);

}
