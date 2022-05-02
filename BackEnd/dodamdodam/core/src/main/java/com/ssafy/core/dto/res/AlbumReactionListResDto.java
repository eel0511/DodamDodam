package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumReactionListResDto {
    @Schema(description = "이모티콘", example = "예시입니다.")
    private String emoticon;

    @Schema(description = "프로필 사진", example = "예시입니다.")
    private String imagePath;

    @Schema(description = "역할", example = "예시입니다.")
    private String role;

    @Schema(description = "프로필id", example = "예시입니다.")
    private long profileId;

    @Schema(description = "리액션id", example = "예시입니다.")
    private long reactionId;

}
