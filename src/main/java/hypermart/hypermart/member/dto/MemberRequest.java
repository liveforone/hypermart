package hypermart.hypermart.member.dto;

import hypermart.hypermart.member.model.MemberGrade;
import hypermart.hypermart.member.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberRequest {

    private Long id;
    private String email;
    private String password;
    private String address;
    private int orderCount;
    private Role auth;
    private MemberGrade memberGrade;
}
