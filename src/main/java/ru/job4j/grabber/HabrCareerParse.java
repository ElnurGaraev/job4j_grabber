package ru.job4j.grabber;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class HabrCareerParse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);
    private static final int PAGES = 5;

    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= PAGES; i++) {
            Connection connection = Jsoup.connect(PAGE_LINK + i);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                Element dateclElement = row.select(".vacancy-card__date").first();
                Element dateTime = dateclElement.child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String date = String.format("%s", dateTime.attr("datetime"));
                System.out.printf("%s %s %s%n", vacancyName, link, date);
            });
        }
    }

    private String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements row = document.select(".vacancy-show");
        Element showElement = row.select(".vacancy-description__text").first();
        Element showDesc = showElement.child(0);
        return String.format("%s", showDesc.attr("style-ugc"));
    }
}
