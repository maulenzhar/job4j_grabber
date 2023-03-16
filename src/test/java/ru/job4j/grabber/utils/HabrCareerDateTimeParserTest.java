package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.*;


class HabrCareerDateTimeParserTest {

    @Test
    void whenDateValid() {
        HabrCareerDateTimeParser date = new HabrCareerDateTimeParser();
        LocalDateTime parsed = date.parse("2023-03-16T08:07:07+03:00");
        assertThat(parsed).isEqualTo("2023-03-16T08:07:07");
    }

}