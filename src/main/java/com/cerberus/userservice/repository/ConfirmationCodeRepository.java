package com.cerberus.userservice.repository;

import com.cerberus.userservice.model.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {

    Optional<ConfirmationCode> findByUser_Id(Long userId);

    void deleteByUser_Id(Long userId);
}
