package dayone.dayone.support;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataCleaner {

    private static final String TRUNCATE_FORMAT = "TRUNCATE TABLE %s";
    private static final String CAMEL_CASE_REGEX = "([a-z])([A-Z]+)";
    private static final String SNAKE_CASE_REGEX = "$1_$2";

    private List<String> tableNames;

    @PersistenceContext
    private EntityManager entityManager;


    @PostConstruct
    public void findDatabaseTableNames() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
            .filter(DataCleaner::isEntityClass)
            .map(DataCleaner::convertCamelCaseToSnakeCase)
            .collect(Collectors.toList());
    }

    private static boolean isEntityClass(final EntityType<?> e) {
        return e.getJavaType().getAnnotation(Entity.class) != null;
    }

    private static String convertCamelCaseToSnakeCase(final EntityType<?> e) {
        return e.getName().replaceAll(CAMEL_CASE_REGEX, SNAKE_CASE_REGEX).toLowerCase();
    }

    @Transactional
    public void clear() {
        entityManager.flush();
        entityManager.clear();
        truncate();
    }

    private void truncate() {
        for (String tableName : tableNames) {
            entityManager.createNativeQuery(String.format(TRUNCATE_FORMAT, tableName))
                .executeUpdate();
        }
    }
}
