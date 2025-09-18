package np.com.debid.ipoinsightplusapi.serviceimpl;


import np.com.debid.ipoinsightplusapi.entity.User;
import np.com.debid.ipoinsightplusapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Error: User Not Found for the email: " + email));

        return UserDetailsImpl.build(userEntity);
    }
}
