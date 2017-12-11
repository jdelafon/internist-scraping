package com.internist.internist;

import java.util.ArrayList;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


class Global {
    final boolean SHOW_CATEGORY = true;
    final boolean SHOW_DATE = false;
    final String ROOT_URL = "http://internist.ru";

    static String todayString() {
        final java.util.Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
        return sdf.format(today);
    }
}

class MyDate {
    private int day;
    private int month;
    private int year;

    MyDate(String date) {
        final String[] months = {
                "января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        Map<String, Integer> monthsMap = new HashMap<>();
        for (int i=1; i <= months.length; i++) {
            monthsMap.put(months[i-1], i);
        }
        String[] parts = date.split(" ");
        this.day = Integer.parseInt(parts[0]);
        this.month = monthsMap.get(parts[1].toLowerCase());
        this.year = Integer.parseInt(parts[2]);
    }

    boolean isWithinRange(int thisMonth, int thisYear) {
        return year == thisYear && month == thisMonth && day <= 25
            || year == thisYear && month == thisMonth-1 && day > 25
            || year == thisYear-1 && month == 12 && thisMonth == 1 && day > 25;
    }
}

class Publication extends Global {
    private String date;
    private String description;
    private String link;

    public String getDescription() {
        return description;
    }

    Publication(String date, String description, String link) {
        this.date = date;
        this.description = description;
        this.link = link;
    }

    public String toString() {
        String base = description + "\n" + ROOT_URL+link + "\n\n";
        if (SHOW_DATE) {
            return date + "\n" + base;
        } else {
            return base;
        }
    }
}


public class Internist extends Global {

    /**
     * Return all possible URL segments following "http://internist.ru/publications/",
     * such as "kardiologiya/".
     */
    Set<String> getCategories() throws IOException {
        Document publicationsPage = Jsoup.connect("http://internist.ru/publications/").get();
        Elements catLinks = publicationsPage.select("a[href~=/publications/([\\w-]+)/$]");
        Set<String> categories = new HashSet<>();
        catLinks.forEach((link) -> categories.add(link.attr("href")));
        return categories;
    }

    /**
     * Return indexes of the dates in `dates` that verify, with respect to `today`,
     * (1) Either same year, same month, days 1-25;
     * (2) or same year, last month, days 26+;
     * (3) or we are in january and we need (1) + the days 26+ of December last year.
     * Return the indices of filtered entries.
     */
    List<Integer> filterIndexesOfTheMonth(List<String> dates, String today) {
        String[] thisParts = today.split(" ");
        int thisMonth = Integer.parseInt(thisParts[1]);
        int thisYear = Integer.parseInt(thisParts[2]);
        List<Integer> validIdx = new ArrayList<>();
        for (int i=0; i < dates.size(); i++) {
            String publicationDate = dates.get(i);
            MyDate r = new MyDate(publicationDate);
            if (r.isWithinRange(thisMonth, thisYear)) {
                validIdx.add(i);
            }
        }
        return validIdx;
    }

    List<Publication> parseInternistWebsite(String category, String today) throws IOException {
        Document site = Jsoup.connect(ROOT_URL + category).get();
        Elements links = site.select("div.b-announcements__text h3 a");
        Elements dates = site.select("div.b-announcements-info span");
        List<String> datesText = dates.stream().map(Element::text).collect(Collectors.toList());

        if (links.size() != dates.size()) {
            System.err.println("Not the same number of links and dates");
            System.exit(1);
        }

        List<Integer> validIdx = filterIndexesOfTheMonth(datesText, today);

        List<Publication> pubs = new ArrayList<>();
        for (int i : validIdx) {
            Element link = links.get(i);
            String date = datesText.get(i);
            String description = link.text();
            Publication pub = new Publication(date, description, link.attr("href"));
            pubs.add(pub);
        }
        return pubs;
    }
    
    public static void main(String[] args) throws IOException {
        Internist in = new Internist();
        final String today = todayString();
        Set<String> categories = in.getCategories();
        for (String category : categories) {
            if (in.SHOW_CATEGORY) {
                System.out.println("-------------------------------------");
                System.out.println("> " + category);
                System.out.println("-------------------------------------");
            }
            List<Publication> pubs = in.parseInternistWebsite(category, today);
            pubs.forEach(System.out::println);
        }
    }
}
