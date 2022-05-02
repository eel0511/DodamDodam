package com.ssafy.api.controller;


import com.ssafy.core.dto.req.AlbumReactionReqDto;
import com.ssafy.core.dto.req.AlbumReqDto;
import com.ssafy.core.dto.res.*;
import com.ssafy.core.entity.*;
import com.ssafy.api.service.AlbumService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ListResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/album")
@Tag(name = "AlbumController", description = "앨범컨트롤러")
public class AlbumController {

    private final AlbumService albumService;
    private final ResponseService responseService;

    //앨범수정, 앨범 검색
    @Operation(summary = "앨범 전체 조회", description = "<strong>앨범 전체 조회</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ListResult<AlbumResDto> getAlbums(Authentication authentication) {
        Long userPK = Long.parseLong(authentication.getName());
        Family family = albumService.findFamilyByUserPK(userPK);
        List<Album> albums = albumService.findAlbumsByFamilyId(family.getId());
        List<AlbumResDto> albumList = new ArrayList<>();

        for (int i = 0; i < albums.size(); i++) {
            Long albumId = albums.get(i).getId();
            AlbumMainResDto main = albumService.findMainPictureByAlbumId(albumId);
            List<HashTag> hashTags = albumService.findHashTagsByAlbumId(albumId);
            AlbumHashTagListResDto albumHashTagListResDto = AlbumHashTagListResDto.builder().build();
            AlbumResDto albumResDto = AlbumResDto.builder()
                    .hashTags(albumHashTagListResDto.fromEntity(hashTags))
                    .mainPicture(main)
                    .build();
            albumList.add(albumResDto);
        }
        return responseService.getListResult(albumList);
    }


    @Operation(summary = "앨범 상세 조회", description = "<strong>앨범 상세 조회</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "{albumId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<AlbumDetailResDto> getAlbum(@PathVariable long albumId, Authentication authentication) {
        Long userPK = Long.parseLong(authentication.getName());
        AlbumPictureListResDto albumPictureListResDto = AlbumPictureListResDto.builder().build();
        AlbumHashTagListResDto albumHashTagListResDto = AlbumHashTagListResDto.builder().build();

        Album album = albumService.findByAlbum(albumId);
        List<AlbumReaction> albumReactions = albumService.findAlbumReactionsByAlbumId(albumId);
        List<HashTag> hashTags = albumService.findHashTagsByAlbumId(albumId);
        List<Picture> pictures = albumService.findPicturesByAlbumId(albumId);

        AlbumDetailResDto albumDetailResDto = AlbumDetailResDto.builder()
                .date(album.getDate().toString())
                .pictures(albumPictureListResDto.fromEntity(pictures))
                .hashTags(albumHashTagListResDto.fromEntity(hashTags))
                .albumReactions(albumService.findReactionList(albumReactions, userPK))

                .build();

        return responseService.getSingleResult(albumDetailResDto);
    }


    @Operation(summary = "앨범 등록", description = "<strong>앨범 등록</strong>", parameters = {
            @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
    })
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult createAlbum(@ModelAttribute
                                    @Valid final AlbumReqDto albumReqDto,
                                    Authentication authentication,
                                    HttpServletRequest request) {

        Long userPK = Long.parseLong(authentication.getName());

        Family family = albumService.findFamilyByUserPK(userPK);

        Album album = Album.builder()
                .family(family)
                .date(albumService.createDate(albumReqDto.getDate()))
                .build();
        albumService.createAlbum(albumReqDto, family, album, albumReqDto.getMultipartFiles(), request);

        return responseService.getSuccessResult();

    }


    @Operation(summary = "앨범 리액션 등록 및 수정", description = "<strong>앨범 리액션 등록 및 수정</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "/reaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult createReaction(
                                       @RequestBody
                                       @io.swagger.v3.oas.annotations.parameters.RequestBody
                                               AlbumReactionReqDto albumReactionReqDto,
                                       Authentication authentication) {

        Long userPK = Long.parseLong(authentication.getName());
        Album album = albumService.findByAlbum(albumReactionReqDto.getAlbumId());
        albumService.manageAlbumReaction(userPK, album, albumReactionReqDto);
        return responseService.getSuccessResult();
    }


    @Operation(summary = "앨범 리액션 삭제", description = "<strong>앨범 리액션 삭제</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @DeleteMapping(value = "/{reacionid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult deleteReaction(@PathVariable long reacionid, Authentication authentication) {

        Long userPK = Long.parseLong(authentication.getName());

        albumService.deleteAlbumReaction(userPK, reacionid);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "앨범 수정", description = "<strong>앨범 수정</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult updateAlbum(
                                    @ModelAttribute
                                    @Valid AlbumReqDto albumReqDto,
                                    Authentication authentication,
                                    HttpServletRequest request) {

        Long userPK = Long.parseLong(authentication.getName());
        Album album = albumService.findByAlbum(albumReqDto.getAlbumId());
        albumService.updateAlbum(userPK, album, albumReqDto, albumReqDto.getMultipartFiles(), authentication, request);
        return responseService.getSuccessResult();
    }

    @GetMapping(value = "/search/{keyword}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "앨범 검색", description = "<strong>앨범 검색</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            }
    )
    public ListResult<AlbumResDto> searchAlbum(@PathVariable String keyword,Authentication authentication){
        Long userPK = Long.parseLong(authentication.getName());
        Family family = albumService.findFamilyByUserPK(userPK);
        List<Album> albums = albumService.findAlbumsByHashTag(keyword, family.getId());
        List<AlbumResDto> albumList = new ArrayList<>();

        for (int i = 0; i < albums.size(); i++) {
            Long albumId = albums.get(i).getId();
            AlbumMainResDto main = albumService.findMainPictureByAlbumId(albumId);
            List<HashTag> hashTags = albumService.findHashTagsByAlbumId(albumId);
            AlbumHashTagListResDto albumHashTagListResDto = AlbumHashTagListResDto.builder().build();
            AlbumResDto albumResDto = AlbumResDto.builder()
                    .hashTags(albumHashTagListResDto.fromEntity(hashTags))
                    .mainPicture(main)
                    .build();
            albumList.add(albumResDto);
        }
        return responseService.getListResult(albumList);
    }


}
