package tech.stark.webapp.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.stark.webapp.models.Account;

import java.util.Optional;

@Service
public class AccountService {

    @PersistenceContext
    private EntityManager entityManager;


    public Optional<Account> getByEmail(String email) {
        try {
            Account account = entityManager.createQuery("SELECT a FROM Account a WHERE a.email = :email", Account.class)
                    .setParameter("email", email)
                    .getSingleResult();

            return Optional.of(account);
        } catch (Exception e) {
            return Optional.empty(); // Account not found
        }
    }
}
