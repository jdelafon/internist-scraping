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
        List<Publication> pubs = internist.parseInternistWebsite(category, 1, today);
        assertEquals(pubs.size(), 1);
        assertTrue(pubs.get(0).getDescription().contains(description));
    }

    @Test
    void test2pages() throws IOException {
        String today = "29 09 2018";
        String category = "/publications/kardiologiya/";
        String description = "Воздействие на ЛПВП - новый горизонт или долгий путь в никуда?";

        // Cannot find it on page 1
        List<Publication> pubs1 = internist.parseInternistWebsite(category, 1, today);
        assertFalse(pubs1.get(0).getDescription().contains(description));

        // But can find it on page 2
        List<Publication> pubs2 = internist.parseInternistWebsite(category, 2, today);
        assertTrue(pubs2.get(2).getDescription().contains(description));
    }

}