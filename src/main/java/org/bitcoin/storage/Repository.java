package org.bitcoin.storage;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.bitcoin.exception.BitcoinDatabaseException;
import org.bitcoin.storage.entity.PriceEntity;
import org.hibernate.NonUniqueObjectException;

import java.util.Optional;
import java.util.logging.Logger;

public class Repository {
    private static final Logger LOGGER = Logger.getLogger(Repository.class.getName());

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

    public void delete(String id) {
        LOGGER.info("Deleting price record by id " + id);
        findById(id).ifPresent(em::remove);
    }

    public void update(PriceEntity price) {
        LOGGER.info("Updating price record " + price.getDateStr());
        em.merge(price);
    }
}