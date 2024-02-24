package com.vn.fpt.projectcapstoneg4.service;

import com.vn.fpt.projectcapstoneg4.model.request.SignUpRequest;
import com.vn.fpt.projectcapstoneg4.model.request.player.CreatePlayerRequest;
import com.vn.fpt.projectcapstoneg4.model.response.ResponseAPI;
import org.springframework.web.multipart.MultipartFile;

public interface PlayerService {
    public ResponseAPI<Object> createPlayer(CreatePlayerRequest request, MultipartFile file);

    public ResponseAPI<Object> getListSearch(String name);

    public ResponseAPI<Object> detailPlayer(long id);

    public ResponseAPI<Object> deletePlayer(long id);

    public ResponseAPI<Object> updatePlayer(CreatePlayerRequest request, MultipartFile file);


}
