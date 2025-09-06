package np.com.debid.ipoinsightplusapi.repository;

import np.com.debid.ipoinsightplusapi.entity.LegacyUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegacyUserRepository extends MongoRepository<LegacyUser, String> {
    List<LegacyUser> findByLegacyUser(Boolean isLegacyUser);
}