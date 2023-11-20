package com.weatherfit.comment_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EntityListeners(AuditingEntityListener.class)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Comment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int boardId;

    @Column
    private String nickname;

    @Column(columnDefinition = "VARCHAR(500) NOT NULL")
    private String content;

    @Column(nullable = true, columnDefinition = "TINYINT(1)")
    @ColumnDefault("1")
    private int status;

    @OneToMany(mappedBy = "comment")
    private List<Reply> replyList;

    @PrePersist
    public void prePersist() {
        this.status = this.status == 0 ? 1 : this.status;
    }
}
