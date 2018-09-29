package com.internist.internist;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class InternistTest {

    private Internist internist = new Internist();

    @Test
    void testGetIndexesOfTheMonth_january() {
        String today = "01 01 2017";
        List<String> dates = Arrays.asList("25 Декабря 2016", "26 декабря 2016", "01 января 2017", "25 января 2017", "26 января 2017");
        List<Integer> indexes = internist.filterIndexesOfTheMonth(dates, today);
        assertEquals(Arrays.asList(1,2,3), indexes);
    }

    @Test
    void testGetIndexesOfTheMonth_february() {
        String today = "01 02 2017";
        List<String> dates = Arrays.asList("25 января 2017", "26 января 2017", "01 февраля 2017", "25 февраля 2017", "26 февраля 2017");
        List<Integer> indexes = internist.filterIndexesOfTheMonth(dates, today);
        assertEquals(Arrays.asList(1,2,3), indexes);
    }

    @Test
    void testGetIndexesOfTheMonth_november() {
        String today = "01 11 2017";
        List<String> dates = Arrays.asList("25 октября 2017", "26 октября 2017", "01 ноября 2017", "25 ноября 2017", "26 ноября 2017");
        List<Integer> indexes = internist.filterIndexesOfTheMonth(dates, today);
        assertEquals(Arrays.asList(1,2,3), indexes);
    }

    @Test
    void testGetIndexesOfTheMonth_december() {
        String today = "01 12 2017";
        List<String> dates = Arrays.asList("25 ноября 2017", "26 ноября 2017", "01 декабря 2017", "25 декабря 2017", "26 декабря 2017");
        List<Integer> indexes = internist.filterIndexesOfTheMonth(dates, today);
        assertEquals(Arrays.asList(1,2,3), indexes);
    }

    @Test
    void testGimnastika() {
        String today = "01 11 2017";
        List<String> dates = Arrays.asList("20 ноября 2017");
        List<Integer> indexes = internist.filterIndexesOfTheMonth(dates, today);
        assertEquals(Arrays.asList(0), indexes);
    }

    @Test
    void testGetCategories() throws IOException {
        // given 
        Set<String> realCategories = new HashSet<>(Arrays.asList(
            "/publications/klinicheskaya-farmakologiya/",
            "/publications/organizatsiya-zdravookhraneniya/",
            "/publications/psikhiatriya/",
            "/publications/nevrologiya/",
            "/publications/travmatologiya/",
            "/publications/genetika/",
            "/publications/otolaringologiya/",
            "/publications/revmatologiya/",
            "/publications/oftalmologiya/",
            "/publications/funktsionalnaya-diagnostika/",
            "/publications/kardiologiya/",
            "/publications/gastroenterologiya/",
            "/publications/pediatriya/",
            "/publications/stomatologiya/",
            "/publications/narkologiya/",
            "/publications/infektsionnye-bolezni/",
            "/publications/sportivnaya-meditsina/",
            "/publications/zhenskoe-zdorove/",
            "/publications/onkologiya/",
            "/publications/anesteziologiya-i-reanimatologiya/",
            "/publications/reabilitologiya/",
            "/publications/gematologiya/",
            "/publications/urologiya/",
            "/publications/pulmonologiya/",
            "/publications/profilakticheskaya-meditsina/",
            "/publications/dermatologiya/",
            "/publications/endokrinologiya/",
            "/publications/laboratornaya-diagnostika/",
            "/publications/allergologiya/",
            "/publications/drugie-spetsialnosti/",
            "/publications/gepatologiya/",
            "/publications/vnutrennie-bolezni/",
            "/publications/khirurgiya/"
        ));

        Set<String> categories = internist.getCategories();
        assertEquals(categories.size(), realCategories.size());
        System.out.println(categories.size());
        assertEquals(categories, realCategories);
    }
}