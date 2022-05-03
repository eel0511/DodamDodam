package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SingIn Response")
public class SignInResDto {

    @Schema(description = "이름", example = "싸피")
    private String name;

    @Schema(description = "엑세스 토큰", example = "SSAFYSSAFY12345")
    private String jwtToken;

    @Schema(description = "리프레쉬 토큰", example = "1234512345SSAFY")
    private String refreshToken;

    @Schema(description = "프로필 id", example = "1 / null")
    private Long profileId;

    @Schema(description = "가족 id", example = "1 /  null")
    private Long familyId;
}