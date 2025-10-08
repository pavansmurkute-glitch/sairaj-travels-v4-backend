package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRoleRepository extends JpaRepository<AdminRole, Long> {
    
    // Find by role name
    Optional<AdminRole> findByRoleName(String roleName);
    
    // Find active roles
    List<AdminRole> findByIsActiveTrueOrderByDisplayName();
    
    // Find active roles (simple method)
    List<AdminRole> findByIsActiveTrue();
    
    // Find all roles ordered by display name
    List<AdminRole> findAllByOrderByDisplayName();
    
    // Check if role name exists
    boolean existsByRoleName(String roleName);
    
    // Check if role name exists (excluding specific role ID for updates)
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM AdminRole r WHERE r.roleName = :roleName AND r.id != :excludeId")
    boolean existsByRoleNameAndIdNot(@Param("roleName") String roleName, @Param("excludeId") Long excludeId);
    
    // Count active roles
    long countByIsActiveTrue();
    
    // Find roles that have specific permission
    @Query("SELECT r FROM AdminRole r WHERE JSON_EXTRACT(r.permissions, :permissionPath) = true")
    List<AdminRole> findRolesWithPermission(@Param("permissionPath") String permissionPath);
    
    // Find roles by search term
    @Query("SELECT r FROM AdminRole r WHERE " +
           "LOWER(r.roleName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.displayName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<AdminRole> findRolesWithSearch(@Param("searchTerm") String searchTerm);
}
