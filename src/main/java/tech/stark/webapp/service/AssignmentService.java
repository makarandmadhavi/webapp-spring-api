package tech.stark.webapp.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
import tech.stark.webapp.controller.error.BadRequestException;
import tech.stark.webapp.controller.error.ServiceUnavailableException;
import tech.stark.webapp.models.Account;
import tech.stark.webapp.models.Assignment;
import tech.stark.webapp.models.Submission;
import tech.stark.webapp.models.TopicMessage;
import tech.stark.webapp.repository.AssignmentRepository;
import tech.stark.webapp.repository.SubmissionRepository;
import tech.stark.webapp.security.SecurityService;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AssignmentService {

    private final Logger LOGGER = LoggerFactory.getLogger(AssignmentService.class);

//    @Value("${aws.sns.topicArn}")
//    private String topic_arn;
    @PersistenceContext
    private EntityManager entityManager;
    private final SecurityService securityService;

    private AccountService accountService;

    private final AssignmentRepository assignmentRepository;

    private final SubmissionRepository submissionRepository;

    @Autowired
    public AssignmentService(SecurityService securityService, AccountService accountService, AssignmentRepository assignmentRepository, SubmissionRepository submissionRepository) {
        this.securityService = securityService;
        this.accountService = accountService;
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
    }

    public  List<Assignment> getAssignments(String id) {
        if(id == null){
            return StreamSupport.stream(
                    assignmentRepository.findAll().spliterator(),
                    false).collect(Collectors.toList());
        } else {
            Optional<Assignment> isAssignment = assignmentRepository.findById(id);
            return isAssignment.map(List::of).orElse(null);
        }

    }

    public Assignment save(Assignment assignment) {
        assignment.setUser_email(securityService.getUser().getUsername());
        String now = Instant.now().toString();
        assignment.setAssignment_created(now);
        assignment.setAssignment_updated(now);
        return assignmentRepository.save(assignment);
    }

    public Optional<Boolean> deleteAssigment(String id) {
        if(assignmentRepository.existsById(id)){
            Assignment oldAssignment = assignmentRepository.findById(id).get();
            if(get_submissions_by_assignment(id).size()>0){
                throw new BadRequestException("Assignment has submissions posted, cannot delete");
            }
            if(oldAssignment.getUser_email().equals(securityService.getUser().getUsername())){
                assignmentRepository.deleteById(id);
                return Optional.of(true);
            } else {
                return null;
            }

        } else {
            return Optional.of(false);
        }
    }

    public Optional<Assignment> updateAssignment(String id, Assignment assignment) {
        if(assignmentRepository.existsById(id)){
            assignment.setId(id);
            Assignment oldAssignment = assignmentRepository.findById(id).get();
            if(oldAssignment.getUser_email().equals(securityService.getUser().getUsername())){
                assignment.setAssignment_created(oldAssignment.getAssignment_created());
                assignment.setUser_email(oldAssignment.getUser_email());
                assignment.setAssignment_updated(Instant.now().toString());
                assignmentRepository.save(assignment);
            } else {
                return null;
            }

            return Optional.of(assignment);
        } else {
            return Optional.empty();
        }
    }

//    public Optional<Submission> postSubmission(Submission submission, String id) {
//        if(!assignmentRepository.existsById(id)){
//            throw new BadRequestException("assignment id "+id+" doesn't exist");
//        }
//        Assignment assignment = assignmentRepository.findById(id).get();
//
//        Instant deadline = Instant.parse(assignment.getDeadline());
//        Instant currentInstant = Instant.now();
//        if(currentInstant.isAfter(deadline)) {
//            throw new BadRequestException("Assignment Deadline passed, you can no longer submit for this assignment");
//        }
//
//        submission.setAssignment_id(id);
//        String now = Instant.now().toString();
//        submission.setSubmission_date(now);
//        submission.setSubmission_updated(now);
//        submission.setUser_email(securityService.getUser().getUsername());
//
//        List<Submission> submissionList = get_submissions_by_user_assignment(submission.getUser_email(), assignment.getId());
//        LOGGER.info(submissionList.toString());
//
//        if(assignment.getNum_of_attempts()<= submissionList.size()){
//            throw new BadRequestException("Number of attempts exceeded for assignment");
//        }
//
//        try {
//            submissionRepository.save(submission);
//
//        } catch (Exception e){
//            LOGGER.error(e.getMessage());
//            throw new ServiceUnavailableException("");
//        }
//        TopicMessage message = new TopicMessage();
//        message.setAttempts(submissionList.size() + 1);
//        message.setSubmission(submission);
//        message.setAccount(accountService.getByEmail(submission.getUser_email()).get());
//        message.setAssignment(assignment);
//        LOGGER.info("Posting message: "+message.toString());
//        try {
//            PublishRequest request = PublishRequest.builder()
//                    .message(message.toString())
//                    .topicArn(topic_arn)
//                    .build();
//
//            PublishResponse result = snsClient.publish(request);
//            LOGGER.info(result.messageId() + " Message sent. Status is " + result.sdkHttpResponse().statusCode());
//
//        } catch (SnsException e) {
//            LOGGER.error(e.awsErrorDetails().errorMessage());
//        }
//        return Optional.of(submission);
//    }

    public List<Submission> get_submissions_by_user_assignment(String user_email, String assignment_id) {
        try {
            List<Submission> submissionsList = entityManager.createQuery("SELECT a FROM Submission a WHERE a.assignment_id = :assignment_id AND a.user_email = :user_email", Submission.class)
                    .setParameter("assignment_id", assignment_id)
                    .setParameter("user_email", user_email)
                    .getResultList();

            return submissionsList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<Submission> get_submissions_by_assignment(String assignment_id) {
        try {
            List<Submission> submissionsList = entityManager.createQuery("SELECT a FROM Submission a WHERE a.assignment_id = :assignment_id", Submission.class)
                    .setParameter("assignment_id", assignment_id)
                    .getResultList();

            return submissionsList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


}
