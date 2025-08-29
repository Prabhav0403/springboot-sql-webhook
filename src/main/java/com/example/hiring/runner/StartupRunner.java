package com.example.hiring.runner;

import com.example.hiring.model.FinalQueryRecord;
import com.example.hiring.model.GenerateWebhookRequest;
import com.example.hiring.model.GenerateWebhookResponse;
import com.example.hiring.repo.FinalQueryRepository;
import com.example.hiring.service.HiringClient;
import com.example.hiring.service.SqlSolver;
import com.example.hiring.util.RegNoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupRunner implements ApplicationRunner {

    private final HiringClient client;
    private final FinalQueryRepository repo;

    @Value("${app.candidate.name}")
    private String name;
    @Value("${app.candidate.regNo}")
    private String regNo;
    @Value("${app.candidate.email}")
    private String email;

    private final @Qualifier("q1Solver") SqlSolver q1Solver;
    private final @Qualifier("q2Solver") SqlSolver q2Solver;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting Hiring Runner...");

        GenerateWebhookRequest req = new GenerateWebhookRequest(name, regNo, email);

        client.generateWebhook(req)
            .flatMap(resp -> {
                log.info("Received webhook info: {}", resp);

                // Pick solver based on last-two-digits odd/even
                boolean odd = RegNoUtil.lastTwoDigitsOdd(regNo);
                SqlSolver solver = odd ? q1Solver : q2Solver;

                String finalQuery = solver.solve();
                log.info("Computed final SQL query:\n{}", finalQuery);

                // Persist locally
                FinalQueryRecord saved = repo.save(
                    FinalQueryRecord.builder()
                        .regNo(regNo)
                        .finalQuery(finalQuery)
                        .createdAt(OffsetDateTime.now())
                        .build()
                );
                log.info("Saved final query with id={}", saved.getId());

                // Submit to webhook using provided JWT
                String jwt = safeGet(resp.getAccessToken());
                if (jwt == null || jwt.isBlank()) {
                    return Mono.error(new IllegalStateException("Missing accessToken in response"));
                }
                return client.submitFinalQuery(jwt, finalQuery)
                             .doOnNext(body -> log.info("Submission response: {}", body));
            })
            .doOnError(err -> log.error("Flow failed", err))
            .block(); // Block once on startup to complete the flow
    }

    private String safeGet(String s) { return (s == null) ? null : s.trim(); }
}
