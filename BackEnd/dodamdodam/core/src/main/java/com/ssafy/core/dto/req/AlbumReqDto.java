package com.ssafy.core.dto.req;


import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO Model : AlbumReqDto")
public class AlbumReqDto {

    @NotNull
    @ArraySchema(schema = @Schema(description = "해시태그", required = true, example = "광화문"))
    private List<String> hashTags;

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(description = "앨범날짜", required = true, example = "2022-04-20")
    private String date;

    @Schema(description = "앨범아이디", required = true, example = "2022-04-20")
    private long albumId;


//    @NotEmpty
//    @Schema(value = "앨범 사진", required = true, example = "")
//    private List<MultipartFile> multipartFiles;

    @NotNull
    @Schema(description = "메인사진 index", required = true, example = "")
    private int mainIndex;

    private List<MultipartFile> multipartFiles;
}
