package com.internist.internist;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class FunctionalTest {

    private Internist internist = new Internist();

    @Test
    void testGimnastika() throws IOException {
        String today = "01 11 2017";
        String category = "/publications/sportivnaya-meditsina/";
        String description = "Гипопрессивная гимнастика: есть ли польза для пациентов?";
        List<Publication> pubs = internist.parseInternistWebsite(category, today);
        assertEquals(pubs.size(), 1);
        assert(pubs.get(0).getDescription().contains(description));
    }
}