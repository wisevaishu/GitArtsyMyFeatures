package MyFeatures.MyFeatures.models.dto;

import MyFeatures.MyFeatures.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class RegisterDTO extends LoginDTO{

    @NotBlank
    @NotNull
    @Email(message = "Invalid email address.")
    private String email;

    @NotNull
    private User.Role role;

    @NotBlank
    @NotNull
    @Size(min = 8, max = 25, message = "Invalid password. Must be between 8 and 25 characters.")
    private String verifyPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }
}