package ru.ohapegor.logFinder.entities;


public enum LogSearchResult {

    Success("Поиск выполнен"),
    DateFromExceedsDateTo("Нижняя граница превосходит верхнюю"),
    DateFromExceedsPresentTime("Некорректное значение нижней границы"),
    IncorrecTimeFormat("Недопустимый формат временного интервала"),
    MissedMandatoryParameter("Не все обязательные поля заполнены"),
    IncorrectResourceName("Неверно указано расположение логов"),
    MissedAsyncMethodFileExcension("Не указано расширение файла для асинхронного метода");

    LogSearchResult(String comment) {
        this.comment = comment;
    }

    private String comment;

    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
