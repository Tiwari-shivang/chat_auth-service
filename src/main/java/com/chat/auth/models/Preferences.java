package com.chat.auth.models;

import com.chat.auth.enums.theme;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "preferences")
@Entity
public class Preferences implements Serializable {
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pid;
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    @OneToOne(fetch = FetchType.LAZY)
    private Users user;
    @Enumerated(EnumType.STRING)
    private theme theme;
    private String primaryClr, secondaryClr;
    private Timestamp createdAt, updatedAt;
}
