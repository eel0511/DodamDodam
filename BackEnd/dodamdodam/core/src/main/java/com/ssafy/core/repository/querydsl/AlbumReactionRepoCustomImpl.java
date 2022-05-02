package com.ssafy.core.repository.querydsl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumReactionRepoCustomImpl implements AlbumReactionRepoCustom{

    private final JPAQueryFactory jpaQueryFactory;

    QAlbum album = QAlbum.album;
    QFamily family = QFamily.family;
    QHashTag hashTag = QHashTag.hashTag;
    QPicture picture = QPicture.picture;
    QAlbumReaction albumReaction = QAlbumReaction.albumReaction;

    @Override
    public List<AlbumReaction> findReactionsByAlbumId(long albumId) {

        return jpaQueryFactory.select(albumReaction)
                .from(albumReaction)
                .leftJoin(albumReaction.album, album)
                .fetchJoin()
                .where(albumReaction.album.id.eq(albumId))
                .fetch();
    }

    @Override
    public AlbumReaction findReactionByAlbumId(long albumId, long profileId) {
        return jpaQueryFactory.select(albumReaction)
                .from(albumReaction)
                .leftJoin(albumReaction.album, album)
                .fetchJoin()
                .where(albumReaction.album.id.eq(albumId).and(albumReaction.profile.id
                        .eq(profileId)))
                .fetchFirst();
    }

    @Override
    public AlbumReaction findReactionByReactionId(long reactionId, long profileId) {

        return jpaQueryFactory.select(albumReaction)
                .from(albumReaction)
                .leftJoin(albumReaction.album, album)
                .fetchJoin()
                .where(albumReaction.id.eq(reactionId).and(albumReaction.profile.id.eq(profileId)))
                .fetchFirst();
    }


}
