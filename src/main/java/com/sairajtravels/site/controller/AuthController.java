package com.sairajtravels.site.controller;

import com.sairajtravels.site.dto.LoginRequest;
import com.sairajtravels.site.dto.LoginResponse;
import com.sairajtravels.site.service.CustomUserDetailsService;
import com.sairajtravels.site.service.UserManagementService;
import com.sairajtravels.site.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${app.cors.allowed-origins:*}")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserManagementService userManagementService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
        } catch (DisabledException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("User account is disabled"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid username or password"));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("authorities", userDetails.getAuthorities());

        return ResponseEntity.ok(new LoginResponse(token, "Login successful"));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    return ResponseEntity.ok(createSuccessResponse("Token is valid"));
                }
            }
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Token validation failed"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // In a stateless JWT implementation, logout is handled client-side
        // by removing the token from storage
        return ResponseEntity.ok(createSuccessResponse("Logout successful"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Email is required"));
            }

            // Create password reset token and send email
            String token = userManagementService.createPasswordResetToken(email);
            
            return ResponseEntity.ok(createSuccessResponse(
                "Password reset link has been sent to your email address. Please check your inbox."
            ));
        } catch (Exception e) {
            // Don't reveal if user exists or not for security
            return ResponseEntity.ok(createSuccessResponse(
                "If the email address exists in our system, you will receive a password reset link."
            ));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String newPassword = request.get("newPassword");

            if (token == null || newPassword == null) {
                return ResponseEntity.badRequest().body(createErrorResponse("Token and new password are required"));
            }

            if (newPassword.length() < 6) {
                return ResponseEntity.badRequest().body(createErrorResponse("Password must be at least 6 characters long"));
            }

            boolean success = userManagementService.resetPasswordWithToken(token, newPassword);
            if (success) {
                return ResponseEntity.ok(createSuccessResponse("Password has been reset successfully. You can now login with your new password."));
            } else {
                return ResponseEntity.badRequest().body(createErrorResponse("Invalid or expired reset token"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to reset password: " + e.getMessage()));
        }
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestParam String token) {
        try {
            boolean valid = userManagementService.validateResetToken(token);
            if (valid) {
                return ResponseEntity.ok(createSuccessResponse("Token is valid"));
            } else {
                return ResponseEntity.badRequest().body(createErrorResponse("Invalid or expired reset token"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Token validation failed: " + e.getMessage()));
        }
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }

    private Map<String, Object> createSuccessResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        return response;
    }
}