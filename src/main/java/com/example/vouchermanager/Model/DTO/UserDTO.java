package com.example.vouchermanager.Model.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {
    private int userId;
    private String userName;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private int roleId;
    private boolean status;

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", roleId=" + roleId +
                ", status=" + status +
                '}';
    }

}
