package tech.stark.webapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.stark.webapp.models.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account,String > {
}
