## [REST API](http://localhost:8080/doc)

## Концепция:

- Spring Modulith
    - [Spring Modulith: достигли ли мы зрелости модульности](https://habr.com/ru/post/701984/)
    - [Introducing Spring Modulith](https://spring.io/blog/2022/10/21/introducing-spring-modulith)
    - [Spring Modulith - Reference documentation](https://docs.spring.io/spring-modulith/docs/current-SNAPSHOT/reference/html/)

```
  url: jdbc:postgresql://localhost:5432/jira
  username: jira
  password: JiraRush
```

- Есть 2 общие таблицы, на которых не fk
    - _Reference_ - справочник. Связь делаем по _code_ (по id нельзя, тк id привязано к окружению-конкретной базе)
    - _UserBelong_ - привязка юзеров с типом (owner, lead, ...) к объекту (таска, проект, спринт, ...). FK вручную будем
      проверять

## Аналоги

- https://java-source.net/open-source/issue-trackers

## Тестирование

- https://habr.com/ru/articles/259055/

Список выполненных задач:

1. Розібрався зі структурою проєкта
2. Видалив соціальні мережі: vk, yandex
3. Винес до окремого проперті файлу: дані доступу до бази, ідентифікатори для OAuth реєстрації/авторизації, налаштування пошти
4. Додав використання для тестів in memory БД (H2), цього потрібно визначити 2 біна, і вибір, який використовувати, визначається активним профілем Spring. Class DataSourceConfig та application-test.yaml
5. Написав 4 тести для публічних методів контролера ProfileRestController
6. Зробив рефакторинг методу com.javarush.jira.bugtracking.attachment.FileUtil#upload - замінено використання класу File на Path та Files.
7. -
8. Додані 2 методи у класі TaskService для підрахунку часу перебування завдання в роботі та у тестуванні. Також знадобився новий метод у інтефейсі ActivityRepository. Додани 3 записи до таблиці ACTIVITY з різними статусами
9. Додано Dockerfile для осовного сервера
10. Написано docker-compose файл для запуску контейнера сервера разом з БД та nginx. Скореговано файл nginx.conf.
11. -
12. -   ...