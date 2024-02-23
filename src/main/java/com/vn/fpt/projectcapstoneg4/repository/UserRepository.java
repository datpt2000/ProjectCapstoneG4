package com.vn.fpt.projectcapstoneg4.repository;

import com.vn.fpt.projectcapstoneg4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository< User, Long> {
    User findByEmailAndDeleteFlg(String email, String deleteFlg);

    User findByIdAndDeleteFlg(long id, String deleteFlg);


    User findByEmailAndVerificationCodeAndDeleteFlg(String email, String code, String deleteFlg);

    List<User> findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseAndDeleteFlg(String firstLike, String lastLike, String deleteFlg);
    boolean existsByEmail(String email);
}
