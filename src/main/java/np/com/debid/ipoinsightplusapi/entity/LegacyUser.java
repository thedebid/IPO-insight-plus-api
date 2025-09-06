package np.com.debid.ipoinsightplusapi.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "legacy_users")
public class LegacyUser {
    @Id
    private String id;

    @Field("email")
    private String email;

    @Field
    private boolean legacyUser = true;
}
