package com.vn.fpt.projectcapstoneg4.repository;

import com.vn.fpt.projectcapstoneg4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository< User, Long> {
    User findByEmailAndDeleteFlg(String email, String deleteFlg);

    boolean existsByEmail(String email);
}
