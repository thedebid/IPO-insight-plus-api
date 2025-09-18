package np.com.debid.ipoinsightplusapi.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "roles")
public class Role {
    @Id
    private Long id;

    @Field
    private String name;
}
