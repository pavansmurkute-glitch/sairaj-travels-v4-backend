package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.PasswordResetToken;
import com.sairajtravels.site.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    // Find by token
    Optional<PasswordResetToken> findByToken(String token);
    
    // Find valid token (not used and not expired)
    @Query("SELECT t FROM PasswordResetToken t WHERE t.token = :token AND t.used = false AND t.expiresAt > :now")
    Optional<PasswordResetToken> findValidToken(@Param("token") String token, @Param("now") LocalDateTime now);
    
    // Find tokens by user
    List<PasswordResetToken> findByUserOrderByCreatedAtDesc(AdminUser user);
    
    // Find tokens by user ID
    @Query("SELECT t FROM PasswordResetToken t WHERE t.user.id = :userId ORDER BY t.createdAt DESC")
    List<PasswordResetToken> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
    
    // Find unused tokens for a user
    @Query("SELECT t FROM PasswordResetToken t WHERE t.user.id = :userId AND t.used = false ORDER BY t.createdAt DESC")
    List<PasswordResetToken> findUnusedTokensByUserId(@Param("userId") Long userId);
    
    // Find expired tokens
    @Query("SELECT t FROM PasswordResetToken t WHERE t.expiresAt < :now AND t.used = false")
    List<PasswordResetToken> findExpiredTokens(@Param("now") LocalDateTime now);
    
    // Delete expired tokens
    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
    
    // Delete used tokens older than specified date
    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken t WHERE t.used = true AND t.usedAt < :cutoffDate")
    void deleteUsedTokensOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // Delete all tokens for a user
    @Modifying
    @Transactional
    void deleteByUser(AdminUser user);
    
    // Delete all tokens for a user by ID
    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken t WHERE t.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
    
    // Count active tokens for a user
    @Query("SELECT COUNT(t) FROM PasswordResetToken t WHERE t.user.id = :userId AND t.used = false AND t.expiresAt > :now")
    long countActiveTokensForUser(@Param("userId") Long userId, @Param("now") LocalDateTime now);
    
    // Mark token as used
    @Modifying
    @Transactional
    @Query("UPDATE PasswordResetToken t SET t.used = true, t.usedAt = :usedAt WHERE t.token = :token")
    void markTokenAsUsed(@Param("token") String token, @Param("usedAt") LocalDateTime usedAt);
}
