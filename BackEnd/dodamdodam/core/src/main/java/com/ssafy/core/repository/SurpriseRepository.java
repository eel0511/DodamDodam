package com.ssafy.core.repository;

import com.ssafy.core.entity.Surprise;
import com.ssafy.core.repository.querydsl.SurpriseRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurpriseRepository extends JpaRepository<Surprise, Long>, SurpriseRepoCustom {
}
