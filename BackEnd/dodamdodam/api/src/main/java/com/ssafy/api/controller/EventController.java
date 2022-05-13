package com.ssafy.api.controller;

import com.ssafy.api.service.EventService;
import com.ssafy.api.service.FamilyService;
import com.ssafy.api.service.ProfileService;
import com.ssafy.api.service.ScheduleService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import com.ssafy.core.dto.req.SurpriseReqDto;
import com.ssafy.core.dto.req.WishTreeReqDto;
import com.ssafy.core.dto.res.WishTreeResDto;
import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.Profile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
@Tag(name = "EventController", description = "이벤트 컨트롤러")
public class EventController {

    private final ResponseService responseService;
    private final EventService eventService;
    private final ProfileService profileService;
    private final FamilyService familyService;
    private final ScheduleService scheduleService;

    @Operation(summary = "위시 리스트생성", description = "<strong>위시 리스트</strong>을 생성한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "/wish-tree")
    public CommonResult createWishTree(@RequestBody
                                           @io.swagger.v3.oas.annotations.parameters.RequestBody
                                           @Valid WishTreeReqDto wishListReq,
                                       Authentication authentication) {
        Long userPk = Long.parseLong(authentication.getName());
        Profile profile = profileService.findProfileByUserPk(userPk);
        Family family = familyService.fromUserIdToFamily(userPk);
        eventService.createWishTree(profile, family, wishListReq);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "위시 리스트 조회", description = "<strong>위시 리스트</strong>을 조회한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "/wish-tree")
    public SingleResult<WishTreeResDto> getWishTree(Authentication authentication) {
        Long userPk = Long.parseLong(authentication.getName());
        Profile profile = profileService.findProfileByUserPk(userPk);
        Family family = familyService.fromUserIdToFamily(userPk);
        return responseService.getSingleResult(eventService.getWishTree(profile, family));
    }

    @Operation(summary = "위시 리스트 수정", description = "<strong>위시 리스트</strong>을 수정한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PatchMapping(value = "/wish-tree/{wishTreeId}")
    public CommonResult updateWishTree(@PathVariable long wishTreeId,
                                       @RequestBody
                                           @io.swagger.v3.oas.annotations.parameters.RequestBody
                                           @Valid WishTreeReqDto wishListReq,
                                       Authentication authentication) {
        Long userPk = Long.parseLong(authentication.getName());
        Profile profile = profileService.findProfileByUserPk(userPk);
        eventService.updateWishTree(profile, wishListReq, wishTreeId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "위시 리스트 삭제", description = "<strong>위시 리스트</strong>을 삭제한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @DeleteMapping(value = "/wish-tree/{wishTreeId}")
    public CommonResult deleteWishList(@PathVariable long wishTreeId,
                                       Authentication authentication) {
        Long userPk = Long.parseLong(authentication.getName());
        Profile profile = profileService.findProfileByUserPk(userPk);
        eventService.deleteWishTree(profile, wishTreeId);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "서프라이즈 파티 생성", description = "<strong>서프라이즈 파티</strong>을 생성한다.",
            parameters = {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "/surprise")
    public CommonResult createSurprise(@RequestBody
                                           @io.swagger.v3.oas.annotations.parameters.RequestBody
                                           @Valid SurpriseReqDto surpriseReq,
                                       Authentication authentication) {
        Long userPk = Long.parseLong(authentication.getName());
        Profile maker = profileService.findProfileByUserPk(userPk);
        LocalDate date = scheduleService.stringToLocalDate(surpriseReq.getDate());
        eventService.createSurprise(maker, surpriseReq, date);
        return responseService.getSuccessResult();
    }
}
