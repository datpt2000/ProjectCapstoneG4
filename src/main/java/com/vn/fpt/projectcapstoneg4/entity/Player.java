package com.vn.fpt.projectcapstoneg4.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "player")
public class Player {
    @Id
    @Column(name = "player_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "name",nullable = false)
    public String name;

    @Column(name = "date_of_birth")
    public Date dateOfBirth;

    @Column(name = "height")
    public Integer height;

    @Column(name = "weight")
    public Integer weight;

    @Column(name = "nationality")
    public String nationality;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "position")
    public String position;

    @Column(name = "bio")
    public String bio;

    @Column(name = "join_date")
    public Date joinDate;

    @Column(name = "delete_flg", length = 1)
    public String deleteFlg;
}
