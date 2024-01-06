package tech.stark.webapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.stark.webapp.models.Account;
import tech.stark.webapp.service.AccountService;

import java.util.Optional;

@Service
public class SecurityService implements UserDetailsService {

    private final AccountService accountService;

    private User user;

    public User getUser() {
        return user;
    }

    @Autowired
    public SecurityService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountService.getByEmail(username);

        Account account = accountOptional.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        this.user = new User(account);
        return user;
    }

}
