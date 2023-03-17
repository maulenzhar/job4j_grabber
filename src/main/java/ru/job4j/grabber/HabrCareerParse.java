package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);

    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= 5; i++) {
            Connection connection = Jsoup.connect(PAGE_LINK + (i == 1 ? "" : "?page=" + i));
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String date = row.select(".vacancy-card__date").first()
                        .child(0).attr("datetime");
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String description = retrieveDescription(link);
                System.out.printf("%s %s %s%n", vacancyName, link, new HabrCareerDateTimeParser().parse(date));
                System.out.println(description);
            });
        }
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
}
