package com.sairajtravels.site.service;

import com.sairajtravels.site.entity.AdminUser;
import com.sairajtravels.site.entity.AdminRole;
import com.sairajtravels.site.entity.PasswordResetToken;
import com.sairajtravels.site.repository.AdminUserRepository;
import com.sairajtravels.site.repository.AdminRoleRepository;
import com.sairajtravels.site.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserManagementService {
    
    @Autowired
    private AdminUserRepository userRepository;
    
    @Autowired
    private AdminRoleRepository roleRepository;
    
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    @Autowired
    private EmailService emailService;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // User Management Methods
    
    public List<AdminUser> getAllUsers() {
        return userRepository.findAll();
    }
    
    public List<AdminUser> getActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }
    
    public Optional<AdminUser> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<AdminUser> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<AdminUser> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<AdminUser> getUserByUsernameOrEmail(String loginId) {
        return userRepository.findByUsernameOrEmail(loginId);
    }
    
    public List<AdminUser> searchUsers(String searchTerm, Boolean isActive) {
        return userRepository.findUsersWithSearch(searchTerm, isActive);
    }
    
    public AdminUser createUser(AdminUser user, Long createdById) {
        // Validate username and email uniqueness
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        
        // Set created by
        if (createdById != null) {
            Optional<AdminUser> creator = userRepository.findById(createdById);
            creator.ifPresent(user::setCreatedBy);
        }
        
        // Generate temporary password if not provided
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            String tempPassword = generateTemporaryPassword();
            user.setPassword(passwordEncoder.encode(tempPassword));
            user.setMustChangePassword(true);
            
        // Send email with temporary password (non-blocking)
        try {
            emailService.sendTemporaryPassword(user.getEmail(), user.getFullName(), 
                                             user.getUsername(), tempPassword);
            System.out.println("✅ Temporary password email sent for user: " + user.getUsername());
        } catch (Exception e) {
            // Log error but don't fail user creation
            System.err.println("⚠️ Failed to send temporary password email (user still created): " + e.getMessage());
            e.printStackTrace();
        }
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        return userRepository.save(user);
    }
    
    public AdminUser updateUser(AdminUser user) {
        Optional<AdminUser> existingUser = userRepository.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        // Check username uniqueness (excluding current user)
        if (userRepository.existsByUsernameAndIdNot(user.getUsername(), user.getId())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check email uniqueness (excluding current user)
        if (userRepository.existsByEmailAndIdNot(user.getEmail(), user.getId())) {
            throw new RuntimeException("Email already exists");
        }
        
        AdminUser existing = existingUser.get();
        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        existing.setFullName(user.getFullName());
        existing.setRole(user.getRole());
        existing.setIsActive(user.getIsActive());
        
        return userRepository.save(existing);
    }
    
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
    
    public void deactivateUser(Long userId) {
        Optional<AdminUser> user = userRepository.findById(userId);
        if (user.isPresent()) {
            AdminUser u = user.get();
            u.setIsActive(false);
            userRepository.save(u);
        }
    }
    
    public void activateUser(Long userId) {
        Optional<AdminUser> user = userRepository.findById(userId);
        if (user.isPresent()) {
            AdminUser u = user.get();
            u.setIsActive(true);
            userRepository.save(u);
        }
    }
    
    // Password Management Methods
    
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        Optional<AdminUser> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }
        
        AdminUser user = userOpt.get();
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);
        userRepository.save(user);
        
        return true;
    }
    
    public boolean resetPassword(Long userId, String newPassword) {
        Optional<AdminUser> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }
        
        AdminUser user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(true);
        userRepository.save(user);
        
        return true;
    }
    
    // Password Reset Token Methods
    
    public String createPasswordResetToken(String email) {
        Optional<AdminUser> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with email: " + email);
        }
        
        AdminUser user = userOpt.get();
        if (!user.getIsActive()) {
            throw new RuntimeException("User account is deactivated");
        }
        
        // Invalidate existing tokens
        tokenRepository.deleteByUserId(user.getId());
        
        // Create new token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1); // 1 hour expiry
        
        PasswordResetToken resetToken = new PasswordResetToken(user, token, expiresAt);
        tokenRepository.save(resetToken);
        
        // Send reset email (non-blocking)
        try {
            emailService.sendPasswordResetEmail(user.getEmail(), user.getFullName(), token);
            System.out.println("✅ Password reset email sent for user: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("⚠️ Failed to send password reset email: " + e.getMessage());
            e.printStackTrace();
            // Don't throw exception - just log the reset token for manual sending
            System.err.println("Manual reset token for " + user.getEmail() + ": " + token);
        }
        
        return token;
    }
    
    public boolean resetPasswordWithToken(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findValidToken(token, LocalDateTime.now());
        if (tokenOpt.isEmpty()) {
            return false;
        }
        
        PasswordResetToken resetToken = tokenOpt.get();
        AdminUser user = resetToken.getUser();
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);
        userRepository.save(user);
        
        // Mark token as used
        resetToken.markAsUsed();
        tokenRepository.save(resetToken);
        
        return true;
    }
    
    public boolean validateResetToken(String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findValidToken(token, LocalDateTime.now());
        return tokenOpt.isPresent();
    }
    
    // Role Management Methods
    
    public List<AdminRole> getAllRoles() {
        return roleRepository.findAllByOrderByDisplayName();
    }
    
    public List<AdminRole> getActiveRoles() {
        return roleRepository.findByIsActiveTrueOrderByDisplayName();
    }
    
    public Optional<AdminRole> getRoleById(Long id) {
        return roleRepository.findById(id);
    }
    
    public Optional<AdminRole> getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
    
    public AdminRole updateRole(AdminRole role) {
        return roleRepository.save(role);
    }
    
    // Authentication Methods
    
    public AdminUser authenticate(String loginId, String password) {
        Optional<AdminUser> userOpt = userRepository.findByUsernameOrEmail(loginId);
        if (userOpt.isEmpty()) {
            return null;
        }
        
        AdminUser user = userOpt.get();
        if (!user.getIsActive()) {
            throw new RuntimeException("Account is deactivated");
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        
        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        return user;
    }
    
    public void updateLastLogin(Long userId) {
        Optional<AdminUser> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            AdminUser user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        }
    }
    
    // Utility Methods
    
    private String generateTemporaryPassword() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
    
    public long getUserCount() {
        return userRepository.count();
    }
    
    public long getActiveUserCount() {
        return userRepository.countByIsActiveTrue();
    }
    
    public List<AdminUser> getUsersByRole(String roleName) {
        return userRepository.findByRoleName(roleName);
    }
}
