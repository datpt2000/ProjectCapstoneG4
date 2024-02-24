package com.vn.fpt.projectcapstoneg4.service.impl;

import com.vn.fpt.projectcapstoneg4.common.CapstoneUtils;
import com.vn.fpt.projectcapstoneg4.common.CommonConstant;
import com.vn.fpt.projectcapstoneg4.entity.Player;
import com.vn.fpt.projectcapstoneg4.entity.User;
import com.vn.fpt.projectcapstoneg4.model.bean.UserBean;
import com.vn.fpt.projectcapstoneg4.model.request.player.CreatePlayerRequest;
import com.vn.fpt.projectcapstoneg4.model.response.ResponseAPI;
import com.vn.fpt.projectcapstoneg4.model.response.player.PlayerResponse;
import com.vn.fpt.projectcapstoneg4.repository.PlayerRepository;
import com.vn.fpt.projectcapstoneg4.service.PlayerService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public ResponseAPI<Object> createPlayer(CreatePlayerRequest request, MultipartFile file) {
        try {
                Player player = new Player();
                if (file.getSize() > 0) {
                    player.setImageUrl(CapstoneUtils.handleAvatar(file));
                }
                player.setName(request.getName());
                player.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(request.getDateOfBirth()));
                player.setHeight(request.getHeight());
                player.setWeight(request.getWeight());
                player.setNationality(request.getNationality());
                player.setPosition(request.getPosition());
                player.setJoinDate(new SimpleDateFormat("yyyy-MM-dd").parse(request.getJoinDate()));
                player.setBio(StringUtils.hasLength(request.getBio()) ? request.getBio() : null);
                player.setDeleteFlg("0");
                playerRepository.saveAndFlush(player);
                return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.OK, "OK");
        } catch (Exception e) {
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.EXCEPTION, CommonConstant.COMMON_MESSAGE.EXCEPTION);
        }
    }

    @Override
    public ResponseAPI<Object> getListSearch(String name) {
            try {
                if (null == name) {
                    name = "";
                }
                List<Player> getListSearch = playerRepository.findAllByNameContainingIgnoreCaseAndDeleteFlg(name, "0");
                if (null != getListSearch && !getListSearch.isEmpty()) {
                    List<PlayerResponse> listResponse = new ArrayList<>();
                    for (Player player : getListSearch) {
                        listResponse.add(new PlayerResponse(player));
                    }
                    return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.OK, CommonConstant.COMMON_MESSAGE.OK, listResponse);
                }
                return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, CommonConstant.COMMON_MESSAGE.EMPTY);
            } catch (Exception e) {
                return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.EXCEPTION, CommonConstant.COMMON_MESSAGE.EXCEPTION);
            }

        }

    @Override
    public ResponseAPI<Object> detailPlayer(long id) {
        try {
            Player player = playerRepository.findByIdAndDeleteFlg(id, "0");
            if (null != player ) {
                return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.OK, CommonConstant.COMMON_MESSAGE.OK, new PlayerResponse(player));
            }
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, "NOT_FIND_PLAYER");
        } catch (Exception e) {
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.EXCEPTION, CommonConstant.COMMON_MESSAGE.EXCEPTION);
        }
    }

    @Override
    public ResponseAPI<Object> deletePlayer(long id) {
        try {
            Player deletePlayer = playerRepository.findByIdAndDeleteFlg(id, "0");
            if (null != deletePlayer) {
                deletePlayer.setDeleteFlg("1");
                playerRepository.saveAndFlush(deletePlayer);
                return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.OK, CommonConstant.COMMON_MESSAGE.OK);
            }
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, "DELETE_ERROR");

        } catch (Exception e) {
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseAPI<Object> updatePlayer(CreatePlayerRequest request, MultipartFile file) {
        try {
            Player player = playerRepository.findByIdAndDeleteFlg(request.getId(), "0");
            if (file.getSize() > 0) {
                player.setImageUrl(CapstoneUtils.handleAvatar(file));
            }
            player.setName(request.getName());
            player.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(request.getDateOfBirth()));
            player.setHeight(request.getHeight());
            player.setWeight(request.getWeight());
            player.setNationality(request.getNationality());
            player.setPosition(request.getPosition());
            player.setJoinDate(new SimpleDateFormat("yyyy-MM-dd").parse(request.getJoinDate()));
            player.setBio(StringUtils.hasLength(request.getBio()) ? request.getBio() : null);
            playerRepository.saveAndFlush(player);
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.OK, "OK");
        } catch (Exception e) {
            return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.EXCEPTION, CommonConstant.COMMON_MESSAGE.EXCEPTION);
        }
    }
}
