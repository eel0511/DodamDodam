package com.ssafy.api.service;

import com.ssafy.core.dto.req.SurpriseReqDto;
import com.ssafy.core.dto.req.WishTreeReqDto;
import com.ssafy.core.dto.res.WishTreeResDto;
import com.ssafy.core.entity.*;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.exception.ErrorCode;
import com.ssafy.core.repository.ProfileRepository;
import com.ssafy.core.repository.SurpriseRepository;
import com.ssafy.core.repository.UserRepository;
import com.ssafy.core.repository.WishTreeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.ssafy.core.exception.ErrorCode.INVALID_REQUEST;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final WishTreeRepository wishTreeRepository;
    private final SurpriseRepository surpriseRepository;
    private final ProfileRepository profileRepository;

    public void createWishTree(Profile profile, Family family, WishTreeReqDto wishTreeReq) {
        if (wishTreeRepository.findWishTreeByProfile(profile) != null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "위시 트리는 하나만 생성 가능합니다.");
        }
        List<Long> usedPosition = wishTreeRepository.findPositionByFamily(family);
        long[] arr = {0,1,2,3,4,5,6,7};
        Long[] longObjects = ArrayUtils.toObject(arr);
        List<Long> allPosition = new ArrayList<>(Arrays.asList(longObjects));
        for (Long position : usedPosition) {
            allPosition.remove(position);
        }
        if (allPosition.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "위시 트리가 가득 찼습니다.");
        }
        Random rand = new Random();

        wishTreeRepository.save(WishTree.builder()
                .profile(profile)
                .family(family)
                .content(wishTreeReq.getContent())
                .position(allPosition.get(rand.nextInt(allPosition.size())))
                .build());
    }

    public WishTreeResDto getWishTree(Profile profile, Family family) {
        List<WishTreeResDto.WishTreeDetail> wishTreeDetailList = wishTreeRepository.findWishTreeListByFamily(family);
        boolean canCreate;
        canCreate = wishTreeRepository.findWishTreeByProfile(profile) == null;
        return WishTreeResDto.builder()
                .wishTree(wishTreeDetailList)
                .canCreate(canCreate)
                .build();
    }

    public void updateWishTree(Profile profile, WishTreeReqDto wishListReq, long wishTreeId) {
        WishTree wishTree = wishTreeRepository.findWishTreeById(wishTreeId);
        if (wishTree == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "해당 위시 트리가 없습니다.");
        }
        if (wishTree.getProfile().getId() != profile.getId()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "해당 권한이 없습니다.");
        }
        wishTree.setContent(wishListReq.getContent());
        wishTreeRepository.save(wishTree);
    }

    public void deleteWishTree(Profile profile, long wishTreeId) {
        WishTree wishTree = wishTreeRepository.findWishTreeById(wishTreeId);
        if (wishTree == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "해당 위시 트리가 없습니다.");
        }
        if (wishTree.getProfile().getId() != profile.getId()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "해당 권한이 없습니다.");
        }
        wishTreeRepository.delete(wishTree);
    }

    public void createSurprise(Profile maker, SurpriseReqDto surpriseReq, LocalDate date) {
        Long targetProfileId = surpriseReq.getTargetProfileId();
        Profile target = profileRepository.findProfileById(targetProfileId);
        if (maker.getFamily().getId() != target.getFamily().getId()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "타 가족 그룹에 권한이 없습니다.");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new CustomException(INVALID_REQUEST, "이미 지난 날짜는 등록이 불가능합니다.");
        }
        surpriseRepository.save(Surprise.builder()
                .maker(maker)
                .target(target)
                .message(surpriseReq.getMessage())
                .date(date)
                .isRead(false)
                .build());
    }
}
