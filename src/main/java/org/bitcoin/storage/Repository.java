package org.bitcoin.storage;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.bitcoin.exception.BitcoinDatabaseException;
import org.bitcoin.storage.entity.PriceEntity;
import org.hibernate.NonUniqueObjectException;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.logging.Logger;

public class Repository {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private final EntityManager em;

    public Repository() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Bitcoin");
        em = emf.createEntityManager();
    }

    public void create(PriceEntity price) throws BitcoinDatabaseException {
        logger.info("Creating price record " + price.getDateStr());
        try {
            em.persist(price);
        } catch (NonUniqueObjectException | EntityExistsException e) {
            throw new BitcoinDatabaseException(e);
        }
    }

    public Optional<PriceEntity> findById(String id) {
        logger.info("Getting price record by id " + id);
        return Optional.ofNullable(em.find(PriceEntity.class, id));
    }

    public void delete(String id) {
        logger.info("Deleting price record by id " + id);
        findById(id).ifPresent(em::remove);
    }

    public PriceEntity update(PriceEntity price) {
        logger.info("Updating price record " + price.getDateStr());
        return em.merge(price);
    }
}