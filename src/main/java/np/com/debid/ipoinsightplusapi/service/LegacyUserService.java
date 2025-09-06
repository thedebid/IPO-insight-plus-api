package np.com.debid.ipoinsightplusapi.service;


import np.com.debid.ipoinsightplusapi.dto.LegacyUserDTO;
import np.com.debid.ipoinsightplusapi.entity.LegacyUser;
import np.com.debid.ipoinsightplusapi.repository.LegacyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegacyUserService {

    @Autowired
    private LegacyUserRepository legacyUserRepository;

    public List<LegacyUser> getAllLegacyUser() {
        return legacyUserRepository.findByLegacyUser(true);
    }

    public LegacyUser createLegacyUser(LegacyUserDTO legacyUserDTO) {
        LegacyUser legacyUser = new LegacyUser();
        legacyUser.setEmail(legacyUserDTO.getEmail());
        return legacyUserRepository.save(legacyUser);
    }

    public LegacyUser findLegacyUserByEmail(String email) {
        return legacyUserRepository.findByLegacyUser(true)
                .stream()
                .filter(legacyUser -> legacyUser.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}
