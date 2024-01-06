package tech.stark.webapp.bootstrap;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.apache.commons.lang3.SystemUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import tech.stark.webapp.models.Account;
import tech.stark.webapp.repository.AccountRepository;
import tech.stark.webapp.security.BcryptEncoder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Component
public class DbBootstrapper implements ApplicationListener<ApplicationReadyEvent> {


    private static final Logger LOGGER = LoggerFactory.getLogger(DbBootstrapper.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BcryptEncoder bcryptEncoder;

    @Value("${application.config.users-csv-path}")
    private String filePath;

    @Value("${application.config.rounds}")
    private int rounds;

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOGGER.info("Bootstrapping database...");
        LOGGER.info("From File "+filePath);
        String cwd = SystemUtils.getUserDir().getAbsolutePath();
        LOGGER.info("Current working directory using SystemUtils: " + cwd);
        try {
            CSVReader reader = new CSVReader(new FileReader(filePath), ',');
            HeaderColumnNameMappingStrategy<Account> beanStrategy = new HeaderColumnNameMappingStrategy<Account>();
            beanStrategy.setType(Account.class);

            CsvToBean<Account> csvToBean = new CsvToBean<Account>();
            List<Account> accounts = csvToBean.parse(beanStrategy, reader);
            accounts.forEach(account -> {
               String hash = bcryptEncoder.encode(account.getPassword());
               account.setPassword(hash);
               String now = Instant.now().toString();
               account.setAccount_created(now);
               account.setAccount_updated(now);
            });
            accounts.forEach(account -> {
                try {
                    accountRepository.save(account);
                } catch (DataIntegrityViolationException e) {
                    LOGGER.error(e.getMessage());
                }
            });

            LOGGER.info(accounts.toString());
            reader.close();

        } catch (FileNotFoundException e) {
            LOGGER.error("Error reading file"+e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
