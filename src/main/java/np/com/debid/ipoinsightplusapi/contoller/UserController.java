package np.com.debid.ipoinsightplusapi.contoller;

import np.com.debid.ipoinsightplusapi.dto.UserRegisterDTO;
import np.com.debid.ipoinsightplusapi.entity.User;
import np.com.debid.ipoinsightplusapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        User createdUser = userService.createUser(userRegisterDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}