package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FamilyPicture Response")
public class FamilyPictureResDto {
    @Schema(description = "사진", example = "/resources/family/asd_mm.png")
    private String picture;
}
