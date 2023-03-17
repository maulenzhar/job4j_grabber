package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private final DateTimeParser dateTimeParser;
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        try {
            for (int i = 1; i <= 5; i++) {
                Connection connection = Jsoup.connect(link + (i == 1 ? "" : "?page=" + i));
                Document document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    Element titleElement = row.select(".vacancy-card__title").first();
                    Element linkElement = titleElement.child(0);
                    String title = titleElement.text();
                    String linkVacancy = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                    String description = retrieveDescription(linkVacancy);
                    String date = row.select(".vacancy-card__date").first()
                            .child(0).attr("datetime");

                    posts.add(new Post(title, linkVacancy, description, dateTimeParser.parse(date)));
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return posts;
    }

    private static String retrieveDescription(String link) {
        StringBuilder description = new StringBuilder();
        try {
            Connection connection = Jsoup.connect(link);
            Document document = connection.get();
            Elements rows = document.select(".basic-section--appearance-vacancy-description");
            rows.forEach(row -> {
                Element descriptionElement = row.select(".faded-content__container").first();
                description.append(descriptionElement.text());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return description.toString();
    }

    public static void main(String[] args) {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        List<Post> list = habrCareerParse.list(PAGE_LINK);
        System.out.println(list);
    }
}
