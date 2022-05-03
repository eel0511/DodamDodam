package com.ssafy.api.controller;

import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.api.dto.req.SuggestionReactionReqDto;
import com.ssafy.api.dto.res.MainProfileResDto;
import com.ssafy.api.dto.res.SuggestionReactionResDto;
import com.ssafy.api.dto.res.SuggestionResDto;
import com.ssafy.api.service.MainService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ListResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainController {

    private final MainService mainService;
    private final ResponseService responseService;
    private final JwtProvider jwtProvider;

    @GetMapping("{familyId}")
    @Parameters({@Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)})
    @Operation(summary = "가족 Profile 정보", description = "본인을 제외한 가족의 <strong>profile<strong> 정보를 받는다.")
    public ListResult<MainProfileResDto> getProfileList(@PathVariable Long familyId, HttpServletRequest request) {

        Long userPk = jwtProvider.getUserPkFromRequest(request);

        return responseService.getListResult(mainService.getProfileList(familyId, userPk));
    }


//  의견 관련 API

    @PostMapping("suggestion")
    @Parameters({@Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)})
    @Operation(summary = "의견 제시 생성", description = "<strong>의견 제시<strong>를 작성한다.")
    public CommonResult createSuggestion(@RequestBody String text, HttpServletRequest request) {

        Long userPk = jwtProvider.getUserPkFromRequest(request);

        mainService.createSuggestion(text, userPk);

        return responseService.getSuccessResult("의견 제시가 정상적으로 등록되었습니다.");
    }

    @GetMapping("suggestion/{familyId}")
    @Parameters({@Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)})
    @Operation(summary = "의견 리스트 목록", description = "<strong>의견 제시 리스트<strong>를 조회한다.")
    public ListResult<SuggestionResDto> getSuggestionList(@PathVariable Long familyId) {

        return responseService.getListResult(mainService.getSuggestionList(familyId));
    }

    @PostMapping("suggestion/reaction")
    @Parameters({@Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)})
    @Operation(summary = "의견 리액션", description = "<strong>의견 제시 리액션<strong>을 등록 혹은 수정한다.")
    public SingleResult<SuggestionReactionResDto> manageSuggestionReaction(@RequestBody SuggestionReactionReqDto request) {

        return responseService.getSingleResult(mainService.manageSuggestionReaction(request));
    }
}