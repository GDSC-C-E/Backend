package gdsc.sc.bsafe.web.dto.request;

import gdsc.sc.bsafe.domain.enums.AIGrade;
import gdsc.sc.bsafe.global.annotation.EnumValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AIRecordRequest {

    @Schema(description = "제목", example = "제목")
    @NotBlank
    String title;

    @Schema(description = "내용", example = "내용")
    @NotBlank
    String detail;

    @EnumValid(enumClass = AIGrade.class, ignoreCase = true, message = "우수 / 보통 / 불량 중 하나의 값을 입력해주세요.")
    @Schema(description = "등급, 우수 / 보통 / 불량 중 하나의 값을 입력합니다.", example = "보통")
    @NotNull
    String grade;

    @Schema(description = "카테고리", example = "카테고리")
    @NotBlank
    String category;

    @NotNull
    MultipartFile image;

}
