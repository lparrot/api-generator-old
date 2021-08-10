package fr.lauparr.apigenerator.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class DaoUtils {

	private static final String SPLIT_CHAR = "\\.";

	/**
	 * Constructeur
	 */
	private DaoUtils() {
	}

	public static <T> T convertToDto(final Object data, final Class<T> clazz) {
		return Objects.requireNonNull(SpringUtils.getBean(ProjectionFactory.class)).createProjection(clazz, data);
	}

	public static <T> List<T> convertListDto(final List<?> liste, final Class<T> clazz) {
		return liste.stream().map(x -> DaoUtils.convertToDto(x, clazz)).collect(Collectors.toList());
	}

	public static <T> Page<T> convertPageDto(final Page<?> page, final Class<T> clazz) {
		return page.map(x -> DaoUtils.convertToDto(x, clazz));
	}

	public static <T, ID> T findRandom(final PagingAndSortingRepository<T, ID> repository) {
		final long count = repository.count();
		final int idx = new SecureRandom().nextInt((int) count);
		final List<T> result = repository.findAll(PageRequest.of(idx, 1)).getContent();
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * Récupère le Path à partir d'un Root
	 *
	 * @return Path
	 */
	public static <T> Path<?> getPathFromRoot(final Root<T> root, final String field) {
		final String principal;
		String[] fields = null;
		if (field != null) {
			final String[] checks = field.split(DaoUtils.SPLIT_CHAR);
			principal = checks[0];
			if (checks.length > 1) {
				fields = Arrays.copyOfRange(checks, 1, checks.length);
			}
		} else {
			principal = null;
		}
		final Path<?> p;
		if (DaoUtils.isSubPath(fields)) {
			p = DaoUtils.crossPathToPath(root.join(principal, JoinType.LEFT), fields);
		} else {
			p = root.get(principal);
		}
		return p;
	}

	/**
	 * Vérifie qu'il ne s'agit pas du dernier Path "extractible"
	 *
	 * @return oui/non
	 */
	private static boolean isSubPath(final String[] fields) {
		return fields != null && fields.length > 0;
	}

	/**
	 * Récupère un Path à partir du Path et de la liste de champs filtrés
	 * (DataLazyModel)
	 *
	 * @return Path
	 */
	private static Path crossPathToPath(final Path path, final String[] fields) {
		Path lastPath = path;
		if (fields != null) {
			for (final String key : fields) {
				lastPath = lastPath.get(key);
			}
		}
		return lastPath;
	}

}
