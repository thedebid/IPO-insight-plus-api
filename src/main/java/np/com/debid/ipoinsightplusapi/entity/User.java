package np.com.debid.ipoinsightplusapi.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("email")
    private String email;

    @Field
    private String password;

    @Field("notification_types")
    private Set<NotificationType> notificationTypes = Set.of(NotificationType.EMAIL);

    @Field("share_issue_types")
    private Set<ShareIssueType> applicableIssue = Set.of(ShareIssueType.GENERAL_IPO);

    @Field
    private boolean legacyUser = false;

    @Field
    private UserStatus status = UserStatus.ACTIVE;

    @Field
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field
    private Set<Role> roles = new HashSet<>();
}

