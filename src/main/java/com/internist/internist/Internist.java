package com.internist.internist;

import java.util.ArrayList;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Internist {
    
    private final boolean DEBUG = false;

    /**
     * Return all possible URL segments following "http://internist.ru/publications/",
     * such as "kardiologiya/".
     */
    private List<String> getCategories() throws IOException {
        Document publicationsPage = Jsoup.connect("http://internist.ru/publications/").get();
        Elements catLinks = publicationsPage.select("a[href~=/publications/(\\w+)/$]");
        List<String> categories = new ArrayList<>();
        catLinks.forEach((link) -> categories.add(link.attr("href")));
        return categories;
    }
    
    /**
     * Keep only entries which date verifies:
     * (1) Either same year, same month, days 1-25;
     * (2) or same year, last month, days 26+;
     * (3) or we are in january and we need (1) + the days 26+ of December last year.
     * Return the indices of filtered entries.
     */
    private List<Integer> getIndexesOfTheMonth(Elements dates) {
        final String[] months = {
            "января", "февраля", "марта", "апреля", "мая", "июня", 
            "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        Map<String, Integer> monthsMap = new HashMap<>();
        for (int i=1; i <= months.length; i++) {
            monthsMap.put(months[i-1], i);
        }
        
        final java.util.Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
        String dateTxt = sdf.format(today);
        
        String[] thisParts = dateTxt.split(" ");
        int thisMonth = Integer.parseInt(thisParts[1]);
        int thisYear = Integer.parseInt(thisParts[2]);
        List<Integer> validIdx = new ArrayList<>();
        for (int i=0; i < dates.size()-1; i++) {
            String date = dates.get(i).text();
            String[] parts = date.split(" ");
            int day = Integer.parseInt(parts[0]);
            int month = monthsMap.get(parts[1].toLowerCase());
            int year = Integer.parseInt(parts[2]);
            if (year == thisYear && month == thisMonth && day <= 25
             || year == thisYear && month == thisMonth-1 && day > 25
             || year == thisYear-1 && month == 12 && thisMonth == 1 && day > 25) {
                validIdx.add(i);
            }
        }
        return validIdx;
    }
    
    private void parse() throws IOException {
        final String rootUrl = "http://internist.ru";
        List<String> categories = getCategories();

        for (String category : categories) {
            if (DEBUG) {
                System.out.println("-------------------------------------");
                System.out.println("> "+ category);
                System.out.println("-------------------------------------");
            }
            
            Document site = Jsoup.connect("http://internist.ru" + category).get();
            Elements links = site.select("div.b-announcements__text h3 a");
            Elements dates = site.select("div.b-announcements-info span");
            
            if (links.size() != dates.size()) {
                System.err.println("Not the same number of links and dates");
                System.exit(1);
            }
            
            List<Integer> validIdx = getIndexesOfTheMonth(dates);
            for (int i : validIdx) {
                Element link = links.get(i);
                Element date = dates.get(i);
                String description = link.text();
                if (DEBUG) { System.out.println(date.text()) ;}
                System.out.println(description);
                System.out.println(rootUrl + link.attr("href"));
                System.out.println("");
            };
        }

    }
    
    public static void main(String[] args) {
        Internist in = new Internist();
        try {
            in.parse();
        } catch (IOException exc) {
            System.err.println("Input source not found: " + exc.getMessage());
        }
    }
}
