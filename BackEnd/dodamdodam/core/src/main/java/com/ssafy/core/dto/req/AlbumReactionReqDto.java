package com.ssafy.core.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO Model : AlbumReactionReqDto")
public class AlbumReactionReqDto {
    @Schema(description = "이모티콘", required = true, example = "아들")
    private String emoticon;
    @Schema(description = "앨범Id", required = true, example = "아들")
    private long albumId;
}
