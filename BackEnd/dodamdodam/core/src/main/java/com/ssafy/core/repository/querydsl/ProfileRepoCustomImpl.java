package com.ssafy.core.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.core.dto.res.MainProfileResDto;
import com.ssafy.core.dto.res.SignInResDto;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.QProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileRepoCustomImpl implements ProfileRepoCustom {
    private final JPAQueryFactory queryFactory;

    QProfile profile = QProfile.profile;

    @Override
    public Profile findProfileByUserPk(Long userPK) {
        return queryFactory
                .selectFrom(profile)
                .where(profile.user.userPk.eq(userPK))
                .fetchFirst();
    }

    @Override
    public List<MainProfileResDto> getProfileListByFamilyId(Long familyId) {
        return queryFactory
                .select(Projections.fields(MainProfileResDto.class,
                        profile.imagePath,
                        profile.role,
                        profile.emotion,
                        profile.comment,
                        profile.user.userPk))
                .from(profile)
                .where(profile.family.id.eq(familyId))
                .fetch();
    }

    @Override
    public SignInResDto findProfileIdAndFamilyIdByUserPk(Long userPk) {
        return queryFactory
                .select(Projections.fields(SignInResDto.class,
                        profile.id.as("profileId"),
                        profile.family.id.as("familyId")))
                .from(profile)
                .where(profile.user.userPk.eq(userPk))
                .fetchFirst();
    }

}
