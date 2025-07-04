package br.com.jurix.querydsql.factory;

import br.com.jurix.querydsql.repository.JoinCapableRepository;
import br.com.jurix.querydsql.repository.impl.JoinCapableRepositoryImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class JoinCapableRepositoryFactory <R extends JpaRepository<T, I>, T, I extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, I> {

    /**
     * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public JoinCapableRepositoryFactory(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {

        return new JoinCapableQueryDslRepository(entityManager);
    }

    private static class JoinCapableQueryDslRepository extends JpaRepositoryFactory {

        private EntityManager entityManager;

        public JoinCapableQueryDslRepository(EntityManager entityManager) {

            super(entityManager);
            this.entityManager = entityManager;
        }

        @Override
        protected Object getTargetRepository(RepositoryInformation metadata) {

            return new JoinCapableRepositoryImpl<>(getEntityInformation(metadata.getDomainType()), entityManager);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

            return JoinCapableRepository.class;
        }
    }
}