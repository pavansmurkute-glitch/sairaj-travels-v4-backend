package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    
    // Find by username
    Optional<AdminUser> findByUsername(String username);
    
    // Find by email
    Optional<AdminUser> findByEmail(String email);
    
    // Find by username or email (for login)
    @Query("SELECT u FROM AdminUser u WHERE u.username = :loginId OR u.email = :loginId")
    Optional<AdminUser> findByUsernameOrEmail(@Param("loginId") String loginId);
    
    // Find active users
    List<AdminUser> findByIsActiveTrue();
    
    // Find users by role
    @Query("SELECT u FROM AdminUser u WHERE u.role.id = :roleId")
    List<AdminUser> findByRoleId(@Param("roleId") Long roleId);
    
    // Find users by role name
    @Query("SELECT u FROM AdminUser u WHERE u.role.roleName = :roleName")
    List<AdminUser> findByRoleName(@Param("roleName") String roleName);
    
    // Find users created by specific user
    List<AdminUser> findByCreatedById(Long createdById);
    
    // Find users who must change password
    List<AdminUser> findByMustChangePasswordTrue();
    
    // Find users who haven't logged in since a certain date
    @Query("SELECT u FROM AdminUser u WHERE u.lastLogin IS NULL OR u.lastLogin < :since")
    List<AdminUser> findUsersNotLoggedInSince(@Param("since") LocalDateTime since);
    
    // Count active users
    long countByIsActiveTrue();
    
    // Count users by role
    @Query("SELECT COUNT(u) FROM AdminUser u WHERE u.role.id = :roleId")
    long countByRoleId(@Param("roleId") Long roleId);
    
    // Check if username exists (excluding specific user ID for updates)
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM AdminUser u WHERE u.username = :username AND u.id != :excludeId")
    boolean existsByUsernameAndIdNot(@Param("username") String username, @Param("excludeId") Long excludeId);
    
    // Check if email exists (excluding specific user ID for updates)
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM AdminUser u WHERE u.email = :email AND u.id != :excludeId")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("excludeId") Long excludeId);
    
    // Find users with search criteria
    @Query("SELECT u FROM AdminUser u WHERE " +
           "(:searchTerm IS NULL OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.role.displayName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "(:isActive IS NULL OR u.isActive = :isActive)")
    List<AdminUser> findUsersWithSearch(@Param("searchTerm") String searchTerm, @Param("isActive") Boolean isActive);
}
