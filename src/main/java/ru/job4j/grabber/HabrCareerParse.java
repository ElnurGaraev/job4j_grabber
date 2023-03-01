package ru.job4j.grabber;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

public class HabrCareerParse implements Parse {
    private final DateTimeParser dateTimeParser;
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);
    private static final int PAGES = 1;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) throws Exception {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        List<Post> posts = habrCareerParse.list(PAGE_LINK);
        for (Post post : posts) {
            System.out.println(post);
        }

        /*for (int i = 1; i <= PAGES; i++) {
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
                    LocalDateTime localDate = new HabrCareerDateTimeParser()
                            .parse(dateTime.attr("datetime"));
                    String description = habrCareerParse.retrieveDescription(link);
                   posts.add(new Post(
                            vacancyName, link, description, localDate));
                    System.out.printf("%s %s %s %s%n", localDate, vacancyName, link, description);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    private String retrieveDescription(String link) {
        String rsl = "";
        Connection connection = Jsoup.connect(link);
        try {
            Document document = connection.get();
            Elements row = document.select(".vacancy-show");
            Element showElement = row.select(".vacancy-description__text").first();
            rsl = showElement.text();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public List<Post> list(String link) {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i <= PAGES; i++) {
            Connection connection = Jsoup.connect(link + i);
            try {
                Document document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    Element titleElement = row.select(".vacancy-card__title").first();
                    Element linkElement = titleElement.child(0);
                    Element dateclElement = row.select(".vacancy-card__date").first();
                    Element dateTime = dateclElement.child(0);
                    String vacancyName = titleElement.text();
                    String vacLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                    LocalDateTime localDate = new HabrCareerDateTimeParser()
                            .parse(dateTime.attr("datetime"));
                    String description = habrCareerParse.retrieveDescription(vacLink);
                    posts.add(new Post(
                            vacancyName, vacLink, description, localDate));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return posts;
    }


        /*List<Post> lists = new ArrayList<>();
        HabrCareerDateTimeParser dateParser = new HabrCareerDateTimeParser();
        Connection connection = Jsoup.connect(link);
        try {
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                Element dateclElement = row.select(".vacancy-card__date").first();
                Element dateTime = dateclElement.child(0);
                String title = titleElement.text();
                String linkStr = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String desc = new HabrCareerParse(dateParser).retrieveDescription(link);
                lists.add(new Post(title, linkStr, desc,
                        dateParser.parse(dateTime.attr("datetime"))));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }*/
}

