package com.arthur.adaptivequizengine.quizSession.entity;

import com.arthur.adaptivequizengine.question.entity.Difficulty;
import com.arthur.adaptivequizengine.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited
@Table(name = "quiz_sessions")
public class QuizSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Difficulty initialDifficulty;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private QuizSessionStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer durationMinutes;

    private Integer score;

    @Version
    @Column(nullable = false)
    private Long version;
}
