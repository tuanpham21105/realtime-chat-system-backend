package com.owl.user_service.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.owl.user_service.persistence.jpa.entity.SignUpAuthenticateCode;

public interface AuthenticateRepository extends JpaRepository<SignUpAuthenticateCode, String> {

}
