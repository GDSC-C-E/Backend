package gdsc.sc.bsafe.web.dto.response;

import gdsc.sc.bsafe.web.dto.common.SliceResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRecordListResponse {

    private SliceResponse<RecordResponse> repairRecord ;

    private SliceResponse<RecordResponse> savedRecord ;

}

