package com.ssafy.core.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ScheduleDetail Response")
public class ScheduleDetailResDto {
    @Schema(description = "id", example = "1")
    private long scheduleId;

    @Schema(description = "제목", example = "엄마 생일파티")
    private String title;

    @Schema(description = "내용", example = "샤넬백 증정하기")
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Schema(description = "역할", example = "아들")
    private String role;
}
