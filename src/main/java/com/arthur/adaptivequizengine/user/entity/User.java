package com.arthur.adaptivequizengine.user.entity;

import com.arthur.adaptivequizengine.answer.entity.Answer;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSession;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Audited
@Table(name = "users")
@ToString(exclude = {"answers", "sessions"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean enabled;

    @Version
    @Column(nullable = false)
    private Long version;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<QuizSession> sessions = new ArrayList<>();
}
