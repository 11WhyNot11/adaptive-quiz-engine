package com.arthur.adaptivequizengine.integration;

import com.arthur.adaptivequizengine.answer.dto.AnswerRequestDto;
import com.arthur.adaptivequizengine.answer.dto.AnswerResponseDto;
import com.arthur.adaptivequizengine.answer.repository.AnswerRepository;
import com.arthur.adaptivequizengine.answer.service.AnswerService;
import com.arthur.adaptivequizengine.question.dto.AnswerOptionDto;
import com.arthur.adaptivequizengine.question.dto.QuestionRequestDto;
import com.arthur.adaptivequizengine.question.dto.QuestionResponseDto;
import com.arthur.adaptivequizengine.question.entity.AnswerOption;
import com.arthur.adaptivequizengine.question.entity.Difficulty;
import com.arthur.adaptivequizengine.question.mapper.QuestionMapper;
import com.arthur.adaptivequizengine.question.repository.AnswerOptionRepository;
import com.arthur.adaptivequizengine.question.repository.QuestionRepository;
import com.arthur.adaptivequizengine.question.service.QuestionService;
import com.arthur.adaptivequizengine.quizSession.dto.AnswerAnalyticsDto;
import com.arthur.adaptivequizengine.quizSession.dto.QuizSessionRequestDto;
import com.arthur.adaptivequizengine.quizSession.dto.QuizSessionResponseDto;
import com.arthur.adaptivequizengine.quizSession.dto.TestResultDto;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSessionStatus;
import com.arthur.adaptivequizengine.quizSession.repository.QuizSessionRepository;
import com.arthur.adaptivequizengine.quizSession.service.QuizSessionService;
import com.arthur.adaptivequizengine.user.entity.Role;
import com.arthur.adaptivequizengine.user.entity.User;
import com.arthur.adaptivequizengine.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class QuizFlowIntegrationTest {

    @Autowired
    private QuizSessionService quizSessionService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuizSessionRepository quizSessionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("test1@gmail.com")
                .password("123456")
                .role(Role.ADMIN)
                .build();
        testUser = userRepository.save(testUser);

        for (int i = 1; i <= 3; i++) {
            int index = i;
            var questionRequestDto = QuestionRequestDto.builder()
                    .text("Question " + i)
                    .topic("Math")
                    .difficulty(i == 1 ? Difficulty.EASY : (i == 2 ? Difficulty.MEDIUM : Difficulty.HARD))
                    .options(List.of(
                            new AnswerOptionDto("Option A", true),
                            new AnswerOptionDto("Option B", false)
                    ))
                    .build();

            questionService.createQuestion(questionRequestDto, testUser);

            var savedQuestion = questionRepository.findByText("Question " + i)
                    .orElseThrow(() -> new RuntimeException("Question not saved"));

            var options = answerOptionRepository.findByQuestion_Id(savedQuestion.getId());

            options.forEach(opt -> System.out.println("Option id for Question " + index + ": " + opt.getId()));
        }
    }



    @Test
    void fullQuizSessionFlow() {
        // 1. Create session
        var sessionDto = QuizSessionRequestDto.builder()
                .initialDifficulty(null)
                .durationMinutes(10)
                .build();
        var session = quizSessionService.createSession(sessionDto, testUser);

        // 2. Start session
        var firstQuestion = quizSessionService.startSession(session.getId(), testUser);
        assertNotNull(firstQuestion);
        assertEquals(Difficulty.MEDIUM, firstQuestion.getDifficulty());

        // 3. Get correct answer (via repo!)
        var correctOption = answerOptionRepository
                .findByQuestion_Id(firstQuestion.getId())
                .stream()
                .filter(AnswerOption::getIsCorrect)
                .findFirst()
                .orElseThrow();

        System.out.println("🧪 Creating answer DTO: questionId=" + firstQuestion.getId() + ", optionId=" + correctOption.getId());
        var answerDto = new AnswerRequestDto(
                firstQuestion.getId(),
                correctOption.getId(),
                session.getId()
        );
        var answer = answerService.submitAnswer(answerDto, testUser);
        assertNotNull(answer);

        // 4. Get next question
        var secondQuestion = quizSessionService.getNextQuestion(session.getId(), testUser);
        if (secondQuestion != null) {
            var correctOption2 = answerOptionRepository
                    .findByQuestion_Id(secondQuestion.getId())
                    .stream()
                    .filter(AnswerOption::getIsCorrect)
                    .findFirst()
                    .orElseThrow();

            var answerDto2 = new AnswerRequestDto(
                    secondQuestion.getId(),
                    correctOption2.getId(),
                    session.getId()
            );

            answerService.submitAnswer(answerDto2, testUser);
        }

        // 5. Finish session
        var resultDto = quizSessionService.finishSession(session.getId(), testUser);
        assertNotNull(resultDto);
        assertEquals(QuizSessionStatus.FINISHED, resultDto.getStatus());

        // 6. Analytics
        var analytics = quizSessionService.getAnalyticsBySession(session.getId(), testUser);
        assertFalse(analytics.isEmpty());
    }


}
