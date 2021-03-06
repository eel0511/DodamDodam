package com.ssafy.core.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.common.ProviderType;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.QProfile;
import com.ssafy.core.entity.QUser;
import com.ssafy.core.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class UserRepoCustomImpl implements UserRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QUser user = QUser.user;
    QProfile profile = QProfile.profile;


    @Override
    public String findUserIdByUserInfo(String name, LocalDate birthday, String familyCode) {
        return jpaQueryFactory.select(user.userId)
                .from(user)
                .join(profile)
                .on(user.userPk.eq(profile.user.userPk))
                .where(user.name.eq(name)
                        .and(user.birthday.eq(birthday))
                        .and(profile.family.code.eq(familyCode))
                        .and(user.providerType.eq(ProviderType.LOCAL)))
                .fetchFirst();
    }

    @Override
    public String findUserFcmTokenByProfile(Profile target) {
        return jpaQueryFactory.select(user.fcmToken)
                .from(user)
                .join(profile)
                .on(user.userPk.eq(profile.user.userPk))
                .where(profile.eq(target))
                .fetchFirst();
    }

    @Override
    public LocalDate findBirthdayByProfileId(Long profileId) {
        return jpaQueryFactory.select(profile.user.birthday)
                .from(profile)
                .join(user)
                .on(profile.user.userPk.eq(user.userPk))
                .where(profile.id.eq(profileId))
                .fetchFirst();
    }

    @Override
    public User findUserByUserIdAndProviderType(String userId, ProviderType providerType) {
        return jpaQueryFactory.select(user)
                .from(user)
                .where(user.userId.eq(userId).and(user.providerType.eq(providerType)))
                .fetchFirst();
    }

    /**
     * QueryDsl ??????
     * 1. cross?????? ????????? -> join??? ?????? ?????? where??? ???????????? ?????? ????????? ??? ?????????.
     *  ?????? ) from(profile).where(profile.family.code.eq(!@#!$))
     *  ?????? ) from(profile).innerJoin(profile).where(profile.family.code.eq(!@#!$))
     *
     * 2. ??????????????? ?????? ??? ???????????? ???. (user?????? profile??? ?????? x, or family?????? profile??? ?????? x)
     *  ?????? findUserByUserInfo?????? .join(profile).on(user.id.eq(profile.user.id))??????
     *  join(target) on(condition)?????? ?????? ?????? ???????????? ?????? ??????!
     *
     * 3. select??? entity??? ???????????? ??????, responseDto??? ????????? ????????? ????????? ????????????!
     *
     */

}

