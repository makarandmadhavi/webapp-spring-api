package tech.stark.webapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.stark.webapp.models.Assignment;

@Repository
public interface AssignmentRepository extends CrudRepository<Assignment,String > {
}
