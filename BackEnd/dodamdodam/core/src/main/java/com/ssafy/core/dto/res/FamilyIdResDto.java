package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FamilyCodeCheck Response")
public class FamilyIdResDto {
    @Schema(description = "id", example = "1")
    private long familyId;
}
