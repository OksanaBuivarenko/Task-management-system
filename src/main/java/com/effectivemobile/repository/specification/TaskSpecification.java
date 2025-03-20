package com.effectivemobile.repository.specification;

import com.effectivemobile.entity.Task;
import com.effectivemobile.entity.Task_;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

public interface TaskSpecification {
    static Specification<Task> idEquals(Long id) {
        return (root, query, cb) -> {
            if (id == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get(Task_.id), id);
        };
    }

    static Specification<Task> like(SingularAttribute<Task, String> field, String value) {
        return (root, query, cb) -> {
            if (value == null) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
        };
    }


    static <T> Specification<Task> like(SingularAttribute<Task, T> table, SingularAttribute<T, String> field,
                                        String value) {
        return (root, query, cb) -> {
            if (value == null) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.join(table).get(field)), "%" + value.toLowerCase() + "%");
        };
    }
}
