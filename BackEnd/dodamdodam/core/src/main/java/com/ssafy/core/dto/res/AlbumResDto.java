package com.ssafy.core.dto.res;


import com.ssafy.core.entity.Album;
import com.ssafy.core.entity.Picture;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResDto {
    @Schema(description = "해시태그", example = "")
    private List<AlbumHashTagListResDto> hashTags;

    @Nullable
    @Schema(description = "메인사진", example = "")
    private AlbumMainResDto mainPicture;
}
