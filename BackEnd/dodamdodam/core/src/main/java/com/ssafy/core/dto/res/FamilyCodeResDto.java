package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FamilyCode Response")
public class FamilyCodeResDto {
    @Schema(description = "코드", example = "A3123AFD33")
    private String code;
}
