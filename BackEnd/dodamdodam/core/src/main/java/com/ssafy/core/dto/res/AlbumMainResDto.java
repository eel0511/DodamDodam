package com.ssafy.core.dto.res;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumMainResDto {

    @Schema(description = "앨범ID", example = "예시입니다.")
    private long albumId;


    @Schema(description = "앨범등록날짜", example = "예시입니다.")
    private LocalDate date;


    @Schema(description = "사진경로", example = "")
    private String imagePath;
}
