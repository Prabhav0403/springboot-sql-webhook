package com.example.hiring.service.impl;

import com.example.hiring.service.SqlSolver;
import org.springframework.stereotype.Component;

/**
 * Solver for Even last-two-digits (Question 2).
 * Replace the placeholder with the real solution SQL once you derive it.
 */
@Component("q2Solver")
public class Question2Solver implements SqlSolver {
@Override
public String solve() {
    String sql = """
        SELECT customer_id, SUM(amount) total
        FROM payments
        WHERE payment_date >= '2024-01-01'
        GROUP BY customer_id
        HAVING SUM(amount) > 1000
        ORDER BY total DESC;
        """;
    return sql.trim();
}

    }
