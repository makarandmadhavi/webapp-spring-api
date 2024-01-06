package tech.stark.webapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.stark.webapp.models.Submission;

@Repository
public interface SubmissionRepository extends CrudRepository<Submission,String > {
}
