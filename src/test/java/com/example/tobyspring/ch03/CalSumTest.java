package com.example.tobyspring.ch03;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
public class CalSumTest {

    Calculator calculator;
    String numFilepath;

    @BeforeEach
    void setUp() {
        this.calculator = new Calculator();
        this.numFilepath = getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    void sumOfNumbers() throws IOException {
        Assertions.assertThat(calculator.calSum(this.numFilepath)).isEqualTo(10);
    }

    @Test
    void multiplyOfNumbers() throws IOException {
        Assertions.assertThat(calculator.calMultiply(this.numFilepath)).isEqualTo(24);
    }

    @Test
    void concatenateStrings() throws IOException {
        Assertions.assertThat(calculator.concatenate(this.numFilepath)).isEqualTo("1234");
    }
}
