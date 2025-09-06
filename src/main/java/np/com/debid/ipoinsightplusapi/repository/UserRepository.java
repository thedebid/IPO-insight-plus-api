package np.com.debid.ipoinsightplusapi.repository;

import np.com.debid.ipoinsightplusapi.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByStatus(String status);
}