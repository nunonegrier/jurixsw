package br.com.jurix.querydsql.repository.impl;

import br.com.jurix.querydsql.descriptor.JoinDescriptor;
import br.com.jurix.querydsql.repository.JoinCapableRepository;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class JoinCapableRepositoryImpl<T, I extends Serializable> extends QueryDslJpaRepository<T, I> implements JoinCapableRepository<T, I> {

    private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;

    private final EntityPath<T> path;
    private final Querydsl querydsl;


    public JoinCapableRepositoryImpl(JpaEntityInformation<T, I> entityInformation, EntityManager entityManager) {
        this(entityInformation, entityManager, DEFAULT_ENTITY_PATH_RESOLVER);
    }

    public JoinCapableRepositoryImpl(JpaEntityInformation<T, I> entityInformation, EntityManager entityManager, EntityPathResolver resolver) {
        super(entityInformation, entityManager, resolver);
        this.path = resolver.createPath(entityInformation.getJavaType());
        PathBuilder<T> builder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
    }

    @Override
    public Page<T> findAll(Predicate predicate, Pageable pageable, JoinDescriptor... joinDescriptors) {
        JPQLQuery countQuery = createFetchQuery(predicate, joinDescriptors);
        JPQLQuery query = querydsl.applyPagination(pageable, createFetchQuery(predicate, joinDescriptors));

        Long total = countQuery.fetchCount();
        List<T> content = total > pageable.getOffset() ? query.fetch() : Collections.emptyList();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<T> findAll(Predicate predicate, Pageable pageable, OrderSpecifier orderSpecifier, JoinDescriptor... joinDescriptors) {
        JPQLQuery countQuery = createFetchQuery(predicate, joinDescriptors);
        JPQLQuery query = querydsl.applyPagination(pageable, createFetchQuery(predicate, orderSpecifier, joinDescriptors));

        Long total = countQuery.fetchCount();
        List<T> content = total > pageable.getOffset() ? query.fetch() : Collections.emptyList();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<T> findAll(Predicate predicate, JoinDescriptor... joinDescriptors) {
        JPQLQuery countQuery = createFetchQuery(predicate, joinDescriptors);
        return countQuery.fetch();
    }

    private JPQLQuery createFetchQuery(Predicate predicate, JoinDescriptor... joinDescriptors) {
        JPQLQuery query = querydsl.createQuery(path);
        for (JoinDescriptor joinDescriptor : joinDescriptors) {
            join(joinDescriptor, query);
        }
        return (JPQLQuery) query.where(predicate);
    }

    private JPQLQuery createFetchQuery(Predicate predicate, OrderSpecifier orderSpecifier, JoinDescriptor... joinDescriptors) {
        JPQLQuery query = querydsl.createQuery(path);
        for (JoinDescriptor joinDescriptor : joinDescriptors) {
            join(joinDescriptor, query);
        }

        query.orderBy(orderSpecifier);

        return (JPQLQuery) query.where(predicate);
    }



    private void join(JoinDescriptor joinDescriptor, JPQLQuery query) {
        switch (joinDescriptor.type) {
            case INNERJOIN:
                if (joinDescriptor.alias != null) {
                    query.innerJoin(joinDescriptor.path, joinDescriptor.alias);
                } else {
                    if(joinDescriptor.path != null) {
                        query.innerJoin(joinDescriptor.path);
                    }else{
                        query.innerJoin(joinDescriptor.listPath);
                    }
                }
                break;
            case LEFTJOIN:
                if (joinDescriptor.alias != null) {
                    query.leftJoin(joinDescriptor.path, joinDescriptor.alias);
                } else {
                    query.leftJoin(joinDescriptor.path);
                }
                break;
            case RIGHTJOIN:
                if (joinDescriptor.alias != null) {
                    query.rightJoin(joinDescriptor.path, joinDescriptor.alias);
                } else {
                    query.rightJoin(joinDescriptor.path);
                }
                break;
            default:
                if (joinDescriptor.alias != null) {
                    query.join(joinDescriptor.path, joinDescriptor.alias);
                } else {
                    query.join(joinDescriptor.path);
                }
                break;
        }
        query.fetchJoin();
    }
}