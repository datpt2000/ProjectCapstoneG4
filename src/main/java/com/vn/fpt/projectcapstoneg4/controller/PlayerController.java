package com.vn.fpt.projectcapstoneg4.controller;

import com.vn.fpt.projectcapstoneg4.common.CommonConstant;
import com.vn.fpt.projectcapstoneg4.model.request.player.CreatePlayerRequest;
import com.vn.fpt.projectcapstoneg4.model.request.user.DeleteUserRequest;
import com.vn.fpt.projectcapstoneg4.model.response.ResponseAPI;
import com.vn.fpt.projectcapstoneg4.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PlayerController extends BaseController {

    @Autowired
    PlayerService playerService;

    @PostMapping(value = CommonConstant.PLAYER_API.CREATE_PLAYER,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseAPI<Object> createPlayer(@RequestPart("request") CreatePlayerRequest request , @RequestPart(value = "file", required = false) MultipartFile file) {
        if(request.isValid()){
            return playerService.createPlayer(request, file);
        }
        return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, CommonConstant.COMMON_MESSAGE.INVALID_PARAMETER);
    }

    @GetMapping(value = CommonConstant.PLAYER_API.GET_LIST_PLAYER)
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseAPI<Object> searchListPlayer(@RequestParam(value = "name", required = false) String name) {
        return playerService.getListSearch(name);
    }

    @GetMapping(value = CommonConstant.PLAYER_API.DETAIL_PLAYER)
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseAPI<Object> detailPlayer(@RequestParam(value = "id") long id) {
        return playerService.detailPlayer(id);
    }

    @PostMapping(value = CommonConstant.PLAYER_API.DELETE_PLAYER)
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseAPI<Object> deletePlayer(@RequestBody DeleteUserRequest request) {
        return playerService.deletePlayer(request.getId());
    }

    @PostMapping(value = CommonConstant.PLAYER_API.UPDATE_PLAYER,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ROLE_A')")
    public ResponseAPI<Object> updatePlayer(@RequestPart("request") CreatePlayerRequest request , @RequestPart(value = "file", required = false) MultipartFile file) {
        if(request.isValidUpdate()){
            return playerService.updatePlayer(request, file);
        }
        return new ResponseAPI<>(CommonConstant.COMMON_RESPONSE.NOT_VALID, CommonConstant.COMMON_MESSAGE.INVALID_PARAMETER);
    }
}
