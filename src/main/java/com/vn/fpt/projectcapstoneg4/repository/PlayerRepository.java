package com.vn.fpt.projectcapstoneg4.repository;

import com.vn.fpt.projectcapstoneg4.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findAllByNameContainingIgnoreCaseAndDeleteFlg(String name, String deleteFlg);
    Player findByIdAndDeleteFlg(long id, String deleteFlg);
}
