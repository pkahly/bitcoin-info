package org.bitcoin.storage;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.bitcoin.bll.model.Transformer;
import org.bitcoin.exception.BitcoinDatabaseException;
import org.bitcoin.storage.entity.PriceEntity;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class Repository implements IRepository {
    private static final Logger LOGGER = Logger.getLogger(Repository.class.getName());
    private static final String DATE_STR = "dateStr";
    private static final String DATE = "date";

    private final EntityManager em;

    public Repository() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Bitcoin");
        em = emf.createEntityManager();
    }

    @Override
    public void create(PriceEntity price) throws BitcoinDatabaseException {
        LOGGER.info("Creating price record " + price.getDateStr());
        try (Transaction ignored = getTransaction()) {
            if (getPrice(price.getDateStr()).isPresent()) {
                throw new BitcoinDatabaseException("Already exists");
            }
            em.persist(price);
        }
    }

    @Override
    public Optional<PriceEntity> getPrice(String dateStr) {
        LOGGER.info("Getting price record: " + dateStr);
        return Optional.ofNullable(em.find(PriceEntity.class, dateStr));
    }

    @Override
    public List<PriceEntity> getPriceRange(String startDateStr, String endDateStr) throws ParseException {
        LOGGER.info(String.format("Loading Price Range: %s - %s", startDateStr, endDateStr));

        // Create Query
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<PriceEntity> query = criteriaBuilder.createQuery(PriceEntity.class);
        Root<PriceEntity> root = query.from(PriceEntity.class);

        // Convert strings to dates
        LocalDate startDate = Transformer.strToDate(startDateStr);
        LocalDate endDate = Transformer.strToDate(endDateStr);
        LOGGER.info("Start Date: " + startDate);
        LOGGER.info("End Date: " + endDate);

        // Where the date is within the range
        query.where(
                criteriaBuilder.greaterThanOrEqualTo(root.get(DATE), startDate),
                criteriaBuilder.lessThanOrEqualTo(root.get(DATE), endDate)
        );

        // Sort by date
        query.orderBy(criteriaBuilder.asc(root.get(DATE_STR)));

        // Execute the query
        return em.createQuery(query).getResultList();
    }

    @Override
    public void update(PriceEntity price) {
        Repository.LOGGER.info("Updating price record " + price.getDateStr());
        try (Transaction ignored = getTransaction()) {
            em.merge(price);
        }
    }

    @Override
    public void delete(String dateStr) {
        LOGGER.info("Deleting price record: " + dateStr);
        try (Transaction ignored = getTransaction()) {
            getPrice(dateStr).ifPresent(em::remove);
        }
    }

    private Transaction getTransaction() {
        // Return a transaction that auto-commits
        return new Transaction(em.getTransaction());
    }
}