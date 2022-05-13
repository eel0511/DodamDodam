package com.ssafy.core.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SurpriseRepoCustomImpl implements SurpriseRepoCustom{
    private final JPAQueryFactory jpaQueryFactory;
}
