package np.com.debid.ipoinsightplusapi.service;


import np.com.debid.ipoinsightplusapi.dto.UserRegisterDTO;
import np.com.debid.ipoinsightplusapi.entity.User;
import np.com.debid.ipoinsightplusapi.entity.UserStatus;
import np.com.debid.ipoinsightplusapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserRegisterDTO userRegisterDTO) {

        User user = new User();
        user.setName(userRegisterDTO.getName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(generateRandomPassword());
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    String generateRandomPassword() {
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    public List<User> getAllActiveUsers() {
        return userRepository.findByStatus("ACTIVE");
    }


}
