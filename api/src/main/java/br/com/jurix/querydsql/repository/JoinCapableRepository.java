package br.com.jurix.querydsql.repository;

import br.com.jurix.querydsql.descriptor.JoinDescriptor;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface JoinCapableRepository <T, I extends Serializable> extends JpaRepository<T, I>, QueryDslPredicateExecutor<T> {

    Page<T> findAll(Predicate predicate, Pageable pageable, JoinDescriptor... joinDescriptors);

    Page<T> findAll(Predicate predicate, Pageable pageable, OrderSpecifier orderSpecifier, JoinDescriptor... joinDescriptors);

    List<T> findAll(Predicate predicate, JoinDescriptor... joinDescriptors);
}
