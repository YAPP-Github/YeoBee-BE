package com.example.yeobee.core.expense.presentation;

import com.example.yeobee.core.auth.annotation.AuthUser;
import com.example.yeobee.core.expense.application.ExpenseService;
import com.example.yeobee.core.expense.dto.request.ExpenseCreateRequestDto;
import com.example.yeobee.core.expense.dto.request.ExpenseUpdateRequestDto;
import com.example.yeobee.core.expense.dto.response.ExpenseCreateResponseDto;
import com.example.yeobee.core.expense.dto.response.ExpenseDetailRetrieveResponseDto;
import com.example.yeobee.core.expense.dto.response.ExpenseUpdateResponseDto;
import com.example.yeobee.core.user.domain.User;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseCreateResponseDto> createExpense(@RequestBody ExpenseCreateRequestDto request) {
        ExpenseCreateResponseDto expenseCreateResponseDto = expenseService.createExpense(request);
        return ResponseEntity.created(URI.create("/v1/expenses/" + expenseCreateResponseDto.id().toString()))
            .body(expenseCreateResponseDto);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseUpdateResponseDto> updateExpense(
        @PathVariable Long expenseId,
        @RequestBody ExpenseUpdateRequestDto request) {
        return ResponseEntity.ok(expenseService.updateExpense(expenseId, request));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseDetailRetrieveResponseDto> retrieveExpenseDetail(
        @PathVariable Long expenseId,
        @AuthUser User user) {
        return ResponseEntity.ok(expenseService.retrieveExpenseDetail(expenseId, user));
    }
}
