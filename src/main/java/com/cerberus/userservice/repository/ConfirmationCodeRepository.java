package com.cerberus.userservice.repository;

import com.cerberus.userservice.model.ConfirmationCode;
import com.cerberus.userservice.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {

    Optional<ConfirmationCode> findByTypeAndUser_Email(Type type, String email);

    void deleteByTypeAndUser_Id(Type type, Long userId);

    Integer countByTypeAndUser_Id(Type type, Long userId);

    void deleteByCode(Integer code);
}
