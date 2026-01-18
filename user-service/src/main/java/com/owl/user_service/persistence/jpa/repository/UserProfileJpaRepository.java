package com.owl.user_service.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.owl.user_service.persistence.jpa.entity.UserProfile;

import java.util.List;

public interface UserProfileJpaRepository extends JpaRepository<UserProfile, String>, JpaSpecificationExecutor<UserProfile> {
    // save(userProfile)
    // findById(id)
    // findAll()
    // deleteById(id)
    // existsById(id)
    // count()
    
    // findByNameContainKeyword
    public List<UserProfile> findByNameContainingIgnoreCase(String keyword);

    //findByGender
    public List<UserProfile> findByGender(Boolean gender);

    //findByDateOfBirth
    public List<UserProfile> findByDateOfBirth(java.time.LocalDate dateOfBirth);

    //findByDateOfBirthBetween
    public List<UserProfile> findByDateOfBirthBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);
    
    //findByEmail
    public List<UserProfile> findByEmail(String email);

    //findByEmailContainingIgnoreCase
    public List<UserProfile> findByEmailContainingIgnoreCase(String emailKeyword);

    //findByPhoneNumber
    public List<UserProfile> findByPhoneNumber(String phoneNumber);

    //findByPhoneNumberContainingIgnoreCase
    public List<UserProfile> findByPhoneNumberContainingIgnoreCase(String phoneNumberKeyword);

    //updateAvatarById
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserProfile a SET a.avatar = :avatar WHERE a.id = :id") 
    public UserProfile updateAvatarById(String id, String avatar);

    //updateUpdatedDateById
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserProfile a SET a.updatedDate = CURRENT_TIMESTAMP WHERE a.id = :id")
    public void updateUpdatedDateById(String id);
}
