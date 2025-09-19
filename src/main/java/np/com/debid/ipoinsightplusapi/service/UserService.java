package np.com.debid.ipoinsightplusapi.service;

import np.com.debid.ipoinsightplusapi.config.JwtUtils;
import np.com.debid.ipoinsightplusapi.dto.LoginRequest;
import np.com.debid.ipoinsightplusapi.dto.LoginResponse;
import np.com.debid.ipoinsightplusapi.dto.UserRegisterDTO;
import np.com.debid.ipoinsightplusapi.entity.User;
import np.com.debid.ipoinsightplusapi.entity.UserStatus;
import np.com.debid.ipoinsightplusapi.exception.CustomException;
import np.com.debid.ipoinsightplusapi.repository.UserRepository;
import np.com.debid.ipoinsightplusapi.serviceimpl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

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


    public LoginResponse authenticateLoginRequest(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            String jwtToken = jwtUtils.generateJwtToken(authentication);

            return LoginResponse.builder()
                    .id(userDetails.getId())
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .roles(roles)
                    .accessToken(jwtToken)
                    .build();
        } catch (BadCredentialsException ex) {
            throw new CustomException("Invalid email or password", 401);
        }
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<User> getAllActiveUsers() {
        return userRepository.findByStatus("ACTIVE");
    }


}
