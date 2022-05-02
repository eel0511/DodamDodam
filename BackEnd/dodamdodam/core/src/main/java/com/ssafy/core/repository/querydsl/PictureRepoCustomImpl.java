package com.ssafy.core.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.dto.res.AlbumMainResDto;
import com.ssafy.core.entity.*;
import com.ssafy.core.entity.Picture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PictureRepoCustomImpl implements PictureRepoCustom{
    private final JPAQueryFactory jpaQueryFactory;
    QAlbum album = QAlbum.album;
    QFamily family = QFamily.family;
    QHashTag hashTag = QHashTag.hashTag;
    QPicture picture = QPicture.picture;
    @Override
    public List<Picture> findPicturesByAlbumId(long albumId) {
        return jpaQueryFactory.select(picture)
                .from(picture)
                .where(picture.album.id.eq(albumId))
                .leftJoin(picture.album, album)
                .fetch();
    }

    @Override
    public Picture findPictureByPictureId(long pictureId) {
        return jpaQueryFactory.select(picture)
                .from(picture)
                .where(picture.id.eq(pictureId))
                .fetchOne();
    }

    @Override
    public AlbumMainResDto findMainPictureByAlbumId(long albumId) {
        AlbumMainResDto result = jpaQueryFactory.select(Projections.fields(AlbumMainResDto.class,
                        picture.album.id.as("albumId"),
                        picture.album.date,
                        picture.path_name.as("imagePath")))
                .from(picture)
                .join(picture.album,album)
                .on(picture.album.id.eq(album.id))
                .where(picture.album.id.eq(albumId).and(picture.is_main.eq(true)))
                .fetchOne();

        return result;
    }
}
