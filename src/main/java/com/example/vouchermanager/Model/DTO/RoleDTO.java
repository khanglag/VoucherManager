package com.example.vouchermanager.Model.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleDTO {
    private int roleId;
    private String roleName;

    @Override
    public String toString() {
        return "RoleDTO{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }

}
