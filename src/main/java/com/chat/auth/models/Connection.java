package com.chat.auth.models;

import com.chat.auth.enums.status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "connections")
public class Connection {
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID conId;
    @JoinColumn(name = "receiverId", referencedColumnName = "uid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users receiverId;
    @JoinColumn(name = "senderId", referencedColumnName = "uid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users senderId;
    @Enumerated(EnumType.STRING)
    private status status;
    private Timestamp createdAt, updatedAt;
}
