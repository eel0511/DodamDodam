package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FamilyJoin Response")
public class FamilyJoinResDto {
    @Schema(description = "코드", example = "A3123AFD33")
    private long familyId;
}
