package com.ssafy.core.repository.querydsl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumRepoCustomImpl implements AlbumRepoCustom{
    private final JPAQueryFactory jpaQueryFactory;

    QAlbum album = QAlbum.album;
    QFamily family = QFamily.family;
    QHashTag hashTag = QHashTag.hashTag;
    QPicture picture = QPicture.picture;
    //앨범 찾기
    //앨범아이디로 해시태그, 사진, 리액션 찾기
    @Override
    public List<Album> findAlbumByFamilyId(long familyId) {

        return jpaQueryFactory.select(album)
                .from(album)
                .where(album.family.id.eq(familyId))
                .leftJoin(album.family, family)
                .fetchJoin()
                .orderBy(album.date.desc())
                .fetch();
    }

    @Override
    public Album findAlbumByAlbumId(long albumId) {

        return jpaQueryFactory.select(album)
                .from(album)
                .where(album.id.eq(albumId))
                .leftJoin(album.family, family)
                .fetchJoin()
                .fetchOne();
    }

    @Override
    public List<Album> findAlbumByHashTag(String keyword, long familyId) {
        return jpaQueryFactory.select(album)
                .from(album)
                .join(family)
                .on(album.family.id.eq(family.id))
                .where(album.family.id.eq(familyId))
                .leftJoin(hashTag)
                .on(album.id.eq(hashTag.album.id))
                .where(hashTag.text.contains(keyword))
                .orderBy(album.date.desc())
                .fetch();
    }






}
