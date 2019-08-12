package io.newage.persistence.domain;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"uuid", "email"})
public class SignupRequest implements Serializable {

    private static final long serialVersionUID = -5351304218739402635L;

    private String uuid;

    private String email;

    private String password;

}
