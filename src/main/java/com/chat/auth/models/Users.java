package com.chat.auth.models;

import com.chat.auth.enums.gender;
import com.chat.auth.enums.roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class Users implements Serializable {
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;
    private String firstName, lastName, userName, email, password;
    @Enumerated(EnumType.STRING)
    private roles role;
    @Enumerated(EnumType.STRING)
    private gender gender;
    private Date dob;
    private Boolean isActive;
    private Timestamp lastLogin, createdAt, updatedAt;
}
