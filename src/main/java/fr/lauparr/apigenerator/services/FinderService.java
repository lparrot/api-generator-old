package fr.lauparr.apigenerator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinderService {

	@Autowired
	private Repositories repositories;

	public <T> T findByClassAndPropertyAndValue(Class<T> clazz, String fieldName, Object value) {
		Map<String, Object> filters = new HashMap<>();
		filters.put(fieldName, value);
		return this.findByClassAndPropertyAndValue(clazz, filters);
	}

	public <T> T findByClassAndPropertyAndValue(Class<T> clazz, Map<String, Object> filters) {
		JpaRepository<T, ?> repository = (JpaRepository) this.repositories.getRepositoryFor(clazz).orElseThrow(() -> new IllegalArgumentException("Impossible de trouver la dao pour la classe " + clazz.getSimpleName()));
		List<T> result = new ArrayList<>();
		try {
			ExampleMatcher matcher = ExampleMatcher.matching();

			T findData = clazz.newInstance();
			for (String fieldName : filters.keySet()) {
				Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(findData, filters.get(fieldName));

				matcher = matcher.withMatcher(fieldName, ExampleMatcher.GenericPropertyMatcher::exact);
			}

			result = repository.findAll(Example.of(findData, matcher));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result.stream().findFirst().orElse(null);
	}

}
