package org.bitcoin.storage;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.bitcoin.exception.BitcoinDatabaseException;
import org.bitcoin.storage.entity.PriceEntity;
import org.hibernate.NonUniqueObjectException;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class Repository {
    private static final Logger LOGGER = Logger.getLogger(Repository.class.getName());
    private static final String DATE_STR = "dateStr";

    private final EntityManager em;

    public Repository() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Bitcoin");
        em = emf.createEntityManager();
    }

    public void create(PriceEntity price) throws BitcoinDatabaseException {
        LOGGER.info("Creating price record " + price.getDateStr());
        try {
            em.persist(price);
        } catch (NonUniqueObjectException | EntityExistsException e) {
            throw new BitcoinDatabaseException(e);
        }
    }

    public Optional<PriceEntity> findById(String id) {
        LOGGER.info("Getting price record by id " + id);
        return Optional.ofNullable(em.find(PriceEntity.class, id));
    }

    public List<PriceEntity> getPriceRange(String startDateStr, String endDateStr) {
        LOGGER.info(String.format("Loading Price Range: %s - %s", startDateStr, endDateStr));

        // Create Query
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<PriceEntity> query = criteriaBuilder.createQuery(PriceEntity.class);
        Root<PriceEntity> root = query.from(PriceEntity.class);

        // Where the date is within the range
        query.where(
                criteriaBuilder.greaterThanOrEqualTo(root.get(DATE_STR), startDateStr),
                criteriaBuilder.lessThanOrEqualTo(root.get(DATE_STR), endDateStr)
        );

        // Sort by date
        query.orderBy(criteriaBuilder.asc(root.get(DATE_STR)));

        // Execute the query
        return em.createQuery(query).getResultList();
    }

    public void delete(String id) {
        LOGGER.info("Deleting price record by id " + id);
        findById(id).ifPresent(em::remove);
    }

    public void update(PriceEntity price) {
        LOGGER.info("Updating price record " + price.getDateStr());
        em.merge(price);
    }
}