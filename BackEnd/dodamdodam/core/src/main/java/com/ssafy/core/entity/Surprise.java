package com.ssafy.core.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "surprise")
public class Surprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 100)
    private String message;

    @Setter
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maker_id")
    private Profile maker;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Profile target;

    @Column(nullable = false)
    private boolean isRead;

}
