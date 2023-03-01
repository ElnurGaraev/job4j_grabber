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
    private Post post(Element row) {
        Element titleElement = row.select(".vacancy-card__title").first();
        Element linkElement = titleElement.child(0);
        Element dateElement = row.select(".vacancy-card__date").first();
        Element dateTime = dateElement.child(0);
        String vacancyName = titleElement.text();
        String vacLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
        LocalDateTime localDate = new HabrCareerDateTimeParser()
                .parse(dateTime.attr("datetime"));
        String description = new HabrCareerParse(new HabrCareerDateTimeParser())
                .retrieveDescription(vacLink);
        return new Post(
                vacancyName, vacLink, description, localDate);
    }

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i <= PAGES; i++) {
            Connection connection = Jsoup.connect(link + i);
            try {
                Document document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> posts.add(post(row)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return posts;
    }
}

