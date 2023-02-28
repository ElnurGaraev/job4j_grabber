package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class HabrCareerDateTimeParserTest {
    @Test
    void whenParseDate() {
        HabrCareerDateTimeParser time = new HabrCareerDateTimeParser();
        String parse = "2023-02-27T12:27:20+03:00";
        LocalDateTime date = time.parse(parse);
        String exp = "2023-02-27T12:27:20";
        assertThat(date).isEqualTo(exp);

    }

}