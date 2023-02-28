package ru.job4j.grabber;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

public class HabrCareerParse extends HabrCareerDateTimeParser {
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);
    private static final int PAGES = 5;

    public static void main(String[] args) throws Exception {
        HabrCareerParse habrCareerParse = new HabrCareerParse();
        for (int i = 1; i <= PAGES; i++) {
                Connection connection = Jsoup.connect(PAGE_LINK + i);
            try {
                Document document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    Element titleElement = row.select(".vacancy-card__title").first();
                    Element linkElement = titleElement.child(0);
                    Element dateclElement = row.select(".vacancy-card__date").first();
                    Element dateTime = dateclElement.child(0);
                    String vacancyName = titleElement.text();
                    String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                    LocalDateTime localDate = habrCareerParse.parse(dateTime.attr("datetime"));
                    String date = String.format("%s", localDate.toString());
                    String description = habrCareerParse.retrieveDescription(link);
                    System.out.printf("%s %s %s %s%n", vacancyName, link, description, date);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public LocalDateTime parse(String parse) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return LocalDateTime.parse(parse, formatter);
    }

    private String retrieveDescription(String link) {
        String rsl = "";
        Connection connection = Jsoup.connect(link);
        try {
            Document document = connection.get();
            Elements row = document.select(".vacancy-show");
            Element showElement = row.select(".vacancy-description__text").first();
            rsl = String.format("%s", showElement.text());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsl;
    }
}

