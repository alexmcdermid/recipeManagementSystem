package recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipes.DTO.RegisterRequest;
import recipes.model.User;
import recipes.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class ResgisterController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {

        if (!isValidEmail(registerRequest.getEmail()) || !isValidPassword(registerRequest.getPassword())) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByUsername(registerRequest.getEmail())) {
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8 && !password.trim().isEmpty();
    }
}
