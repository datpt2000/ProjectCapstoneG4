package com.vn.fpt.projectcapstoneg4.model.response.player;

import com.vn.fpt.projectcapstoneg4.entity.Player;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import java.util.Date;

@Data
public class PlayerResponse {
    private long id;
    private String name;
    public Date dateOfBirth;
    public Integer height;
    public Integer weight;
    public String nationality;
    private String imageUrl;
    public String position;
    public String bio;
    public Date joinDate;
    public String deleteFlg;

    public PlayerResponse(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.dateOfBirth = player.getDateOfBirth();
        this.height = player.getHeight();
        this.weight = player.getWeight();
        this.nationality = player.getNationality();
        if(StringUtils.hasLength(player.getImageUrl())){
            this.imageUrl = player.getImageUrl();
        }
        this.position = player.getPosition();
        if(StringUtils.hasLength(player.getBio())){
            this.bio = player.getBio();
        }
        this.joinDate = player.getJoinDate();
        this.deleteFlg = player.getDeleteFlg();
    }
}
