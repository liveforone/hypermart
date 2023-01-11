package hypermart.hypermart.member.dto;

import hypermart.hypermart.member.model.MemberGrade;
import hypermart.hypermart.member.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String email;
    private Role auth;
    private MemberGrade memberGrade;
    private int orderCount;
    private String address;
}
