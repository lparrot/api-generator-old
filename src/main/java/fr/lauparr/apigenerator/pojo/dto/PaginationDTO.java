package fr.lauparr.apigenerator.pojo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PaginationDTO {
	private List<Object> list = new ArrayList<>();
	private int size = 15;
	private int page = 1;
	private int total = 0;

	@Builder
	public PaginationDTO(List<Object> list, int size, int page, int total) {
		this.list = list;
		this.size = size;
		this.page = page;
		this.total = total;
	}
}
