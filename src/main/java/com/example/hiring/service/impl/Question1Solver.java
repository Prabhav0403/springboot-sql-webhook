package com.example.hiring.service.impl;

import com.example.hiring.service.SqlSolver;
import org.springframework.stereotype.Component;

/**
 * Solver for Odd last-two-digits (Question 1).
 * Replace the placeholder with the real solution SQL once you derive it.
 */
@Component("q1Solver")
public class Question1Solver implements SqlSolver {
    @Override
    public String solve() {
        // TODO: Put your actual final SQL here after reading the assignment.
        // This placeholder lets the pipeline run end-to-end.
        return "SELECT 1 AS answer_for_q1;";
    }
}
