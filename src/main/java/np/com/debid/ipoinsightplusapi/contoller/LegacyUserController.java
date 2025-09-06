package np.com.debid.ipoinsightplusapi.contoller;

import np.com.debid.ipoinsightplusapi.dto.LegacyUserDTO;
import np.com.debid.ipoinsightplusapi.entity.LegacyUser;
import np.com.debid.ipoinsightplusapi.service.LegacyUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/legacy/users")
public class LegacyUserController {

    private static final Logger logger = LoggerFactory.getLogger(LegacyUserController.class);

    @Autowired
    private LegacyUserService legacyUserService;

    @GetMapping
    public ResponseEntity<?> getAllLegacyUser() {
        return new ResponseEntity<>(legacyUserService.getAllLegacyUser(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody LegacyUserDTO legacyUserDTO) {
        LegacyUser legacyUser = legacyUserService.findLegacyUserByEmail(legacyUserDTO.getEmail());
        if(Objects.nonNull(legacyUser)) {
            logger.info("Found legacy user with email {}", legacyUserDTO.getEmail());
            return new ResponseEntity<>("User with this email already exists as a legacy user.", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(legacyUserService.createLegacyUser(legacyUserDTO), HttpStatus.CREATED);
    }
}