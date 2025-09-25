package np.com.debid.ipoinsightplusapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LegacyUserDTO {
    @NotBlank(message = "Email is required")
    private String email;
}
