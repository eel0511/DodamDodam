package com.ssafy.core.repository.querydsl;

import com.ssafy.core.dto.res.AlbumMainResDto;
import com.ssafy.core.entity.Picture;

import java.util.List;

public interface PictureRepoCustom {

    List<Picture> findPicturesByAlbumId(long albumId);
    Picture findPictureByPictureId(long pictureId);
    AlbumMainResDto findMainPictureByAlbumId(long albumId);
}
