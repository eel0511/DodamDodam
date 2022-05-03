package com.ssafy.core.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.entity.*;
import com.ssafy.core.entity.Family;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FamilyRepoCustomImpl implements FamilyRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QUser user = QUser.user;
    QProfile profile = QProfile.profile;
    QFamily family = QFamily.family;

    @Override
    public Family findFamilyByUserPk(Long userPk) {
        return jpaQueryFactory.select(family)
                .from(family)
                .join(profile)
                .on(family.id.eq(profile.family.id))
                .join(user)
                .on(profile.user.userPk.eq(user.userPk))
                .where(user.userPk.eq(userPk))
                .fetchFirst();
    }

    @Override
    public Long findFamilyIdByUserPk(Long userPk) {
        return jpaQueryFactory.select(family.id)
                .from(family)
                .join(profile)
                .on(profile.family.id.eq(family.id))
                .join(user)
                .on(profile.user.userPk.eq(user.userPk))
                .where(user.userPk.eq(userPk))
                .fetchFirst();
    }
}
