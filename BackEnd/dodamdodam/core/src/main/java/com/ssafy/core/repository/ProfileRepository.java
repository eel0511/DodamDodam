package com.ssafy.core.repository;

import com.ssafy.core.entity.Profile;
import com.ssafy.core.repository.querydsl.ProfileRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfileRepository extends JpaRepository<Profile, Long>, ProfileRepoCustom {

}
