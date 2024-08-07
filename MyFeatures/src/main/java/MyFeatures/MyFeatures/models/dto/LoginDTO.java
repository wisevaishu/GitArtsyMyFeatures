package MyFeatures.MyFeatures.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginDTO {

    @NotBlank
    @NotNull
    @Size(min = 3, max = 15, message = "Invalid username. Must be between 3 and 15 characters.")
    private String username;

    @NotBlank
    @NotNull
    @Size(min = 8, max = 25, message = "Invalid password. Must be between 8 and 25 characters.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}