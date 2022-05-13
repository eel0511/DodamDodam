package com.ssafy.core.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : SurpriseReqDto")
public class SurpriseReqDto {

    @Schema(description = "대상 id", required = true, example = "2")
    private Long targetProfileId;

    @Schema(description = "메시지", required = true, example = "생일 축하드려요 엄마!")
    private String message;

    @Schema(description = "날짜", required = true, example = "2022-05-23")
    private String date;
}
