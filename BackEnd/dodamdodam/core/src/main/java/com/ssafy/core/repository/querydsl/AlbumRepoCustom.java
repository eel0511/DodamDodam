package com.ssafy.core.repository.querydsl;

import com.ssafy.core.entity.Album;

import java.util.List;

public interface AlbumRepoCustom {

    List<Album> findAlbumByFamilyId(long familyId);
    Album findAlbumByAlbumId(long albumId);

    List<Album> findAlbumByHashTag(String hashTag, long familyId);

}
