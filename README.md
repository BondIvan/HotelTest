Небольшое RESTful API для работы с отелями

##### Используемые технологии:
* Java 21
* Spring boot 3
* Spring data jpa
* Spring validation
* Liquibase
* H2 database
* Maven
* Lombok

##### Используемые паттерны:
* DTO & mapper pattern
* repository pattern
* specification pattern
---
##### Для запуска приложения необходимо:
* установить java 21+
* установить maven (официальный сайт - https://maven.apache.org/download.cgi)
* склонировать репозиторий
* запустить с помощью команды ```mvn spring-boot:run```

Приложение запускается на порту `8092` \
Все пути начинаются с `/property-view` \
Эндпоинты:
* `GET /hotels` - получение краткой информации об отеле
* `GET /hotels/{id}` - получение полной информации об отеле
* `POST /hotels` - создание нового отеля
* `POST /hotels/{id}/amenities` - добавление списка удобств для отеля
* `GET /search` - поиск по критериям и получение списка отелей с их краткой информацией  
* `GET /histogram/{param}` - получение гистограммы по критерию

##### Для переключения на другую базу данных:
* добавить зависимость в `pom.xml` файл
* ввести данные для подключения в файл `src/main/resources/application-template.yaml`
* заменить в файле `application.yaml` spring-профиль (spring.profiles.active) с `dev` на `template`