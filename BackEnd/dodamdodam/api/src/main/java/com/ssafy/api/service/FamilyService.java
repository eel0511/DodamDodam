package com.ssafy.api.service;

import com.ssafy.core.dto.req.FamilyCreateReqDto;
import com.ssafy.core.dto.req.FamilyJoinReqDto;
import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.User;
import com.ssafy.core.exception.CustomErrorCode;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.FamilyRepository;
import com.ssafy.core.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

import static com.ssafy.core.exception.CustomErrorCode.DUPLICATE_NICKNAME;
import static com.ssafy.core.exception.CustomErrorCode.DUPLICATE_ROLE;

@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyService {
    private final FamilyRepository familyRepository;
    private final ProfileRepository profileRepository;
    private final FileService fileService;

    // 가족 생성 및 프로필 생성
    public Family createFamily() {
        String key;
        for (int i = 0; true; i++) {
            Random rnd = new Random();
            key = "";
            for (int j = 0; j < 15; j++) {
                if (rnd.nextBoolean()) {
                    key += ((char) ((int) (rnd.nextInt(26)) + 65));
                } else {
                    key += (rnd.nextInt(10));
                }
            }
            if (familyRepository.findFamilyByCode(key) == null) {
                break;
            }
        }
        return familyRepository.save(Family.builder()
                .code(key)
                .build());
    }

    // profile 생성
    public void createProfileForFirst(Family family, User user, FamilyCreateReqDto familyRequest, String[] imageInfo) {
        Profile profile = Profile.builder()
                .role(familyRequest.getRole())
                .nickname(familyRequest.getNickname())
                .imagePath(imageInfo[0])
                .imageName(imageInfo[1])
                .user(user)
                .family(family)
                .build();
        profileRepository.save(profile);
    }
    public void createProfileForJoin(Family family, User user, FamilyJoinReqDto familyRequest, String[] imageInfo) {
        Profile profile = Profile.builder()
                .role(familyRequest.getRole())
                .nickname(familyRequest.getNickname())
                .imagePath(imageInfo[0])
                .imageName(imageInfo[1])
                .user(user)
                .family(family)
                .build();
        profileRepository.save(profile);
    }

    // family_id로 Family 객체 조회
    public Family getFamily(long familyId) {
        Family family = familyRepository.findFamilyById(familyId);
        if (family == null) {
            throw new CustomException(CustomErrorCode.INVALID_REQUEST, "해당 그룹이 없습니다.");
        }
        return family;
    }

    // code로 family 조회
    public Family checkCode(String code) {
        Family family = familyRepository.findFamilyByCode(code);
        if (family == null) {
            throw new CustomException(CustomErrorCode.INVALID_REQUEST, "해당 그룹이 없습니다.");
        }
        return family;
    }


    public void familyExistCheck(Long userPK) {
        if (profileRepository.findProfileByUserPk(userPK) != null) {
            throw new CustomException(CustomErrorCode.INVALID_REQUEST, "이미 가입된 그룹이 있습니다.");
        }
    }

    public Family fromUserIdToFamily(Authentication authentication) {
        Long userPk = Long.parseLong(authentication.getName());
        Profile profile = profileRepository.findProfileByUserPk(userPk);
        if (profile == null) {
            throw new CustomException(CustomErrorCode.INVALID_REQUEST, "소속된 그룹이 없습니다.");
        }
        long familyId = profile.getFamily().getId();
        return familyRepository.findFamilyById(familyId);
    }

    public void updateFamilyPicture(Family family, MultipartFile picture, String path) {
        String originFileName = picture.getOriginalFilename();
        String filePath = fileService.uploadFileV1("family",picture);
//        String originFileName = picture.getOriginalFilename();
//        UUID uuid = UUID.randomUUID();
//        String saveFileName = "/resources/familyPicture/" + uuid.toString() + "_" + originFileName;
//        File dir = new File(path + "/resources/familyPicture");
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        try {
//            log.info(family.getPicture());
//            if (family.getPicture() != null) {
//                File file = new File(path + family.getPicture());
//                log.info(file.toString());
//                file.delete();
//            }
//            File file = new File(path + saveFileName);
//            picture.transferTo(file);
//        } catch (Exception e) {
//            throw new CustomException(CustomErrorCode.INVALID_REQUEST, "파일이 없습니다.");
//        }
        family.setPicture(filePath);
        familyRepository.save(family);
    }

    public void checkFamilyAuthority(Authentication authentication) {
        Long userPk = Long.parseLong(authentication.getName());
        Profile profile = profileRepository.findProfileByUserPk(userPk);
        if (profile == null) {
            throw new CustomException(CustomErrorCode.INVALID_REQUEST, "그룹에 권한이 없습니다.");
        }
    }

    public Long getFamilyIdByUserPk(Long userPk) {
        Long familyId = familyRepository.findFamilyIdByUserPk(userPk);
        if (familyId == null) {
            throw new CustomException(CustomErrorCode.NOT_BELONG_FAMILY);
        }
        return familyId;
    }

}
