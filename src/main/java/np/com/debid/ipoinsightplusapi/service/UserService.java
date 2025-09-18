package np.com.debid.ipoinsightplusapi.service;
import np.com.debid.ipoinsightplusapi.dto.UserRegisterDTO;
import np.com.debid.ipoinsightplusapi.entity.User;
import np.com.debid.ipoinsightplusapi.entity.UserStatus;
import np.com.debid.ipoinsightplusapi.exception.CustomException;
import np.com.debid.ipoinsightplusapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(UserRegisterDTO userRegisterDTO) {
        User existingUser = findUserByEmail(userRegisterDTO.getEmail());
        if (Optional.ofNullable(existingUser).isPresent()) {
            throw new CustomException("User with email " + userRegisterDTO.getEmail() + " already exists", 409);
        }
        User user = new User();
        user.setName(userRegisterDTO.getName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<User> getAllActiveUsers() {
        return userRepository.findByStatus("ACTIVE");
    }


}
