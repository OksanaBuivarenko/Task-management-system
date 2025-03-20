# Task-management-system

Task Management System - это простая система управления задачами с использованием Java, Spring. 
Система обеспечивает создание, редактирование, удаление и просмотр задач. Каждая задача содержит заголовок, описание,
статус , приоритет и комментарии, а также автора задачи и исполнителя. 

## Основные функции:

### Аутентификация и авторизация

Безопасность входа в приложение обеспечивается Spring Security с помощью JWT токена по email и паролю. Это позволяет ограничить вход для 
не аутентифицированных пользователей, а также разграничить пользовательские роли.

### Управление задачами 

Администратор может управлять всеми задачами: создавать новые, редактировать существующие, просматривать и удалять, менять статус и приоритет,
назначать исполнителей задачи, оставлять комментарии.
Пользователи могут управлять своими задачами, если указаны как исполнитель: менять статус, оставлять комментарии.
API позволяет получать задачи конкретного автора или исполнителя, а также все комментарии к ним. 

## Начало работы

1. Установите на свой компьютер [JDK](https://www.oracle.com/cis/java/technologies/downloads/) и среду
разработки [IntelliJ IDEA](https://www.jetbrains.com/ru-ru/idea/download/?section=windows), если они ещё не 
установлены.

2. Загрузите проект-заготовку из Git-репозитория.

3. Запустите базы данных Postgres и Redis, выполнив в терминале команду `docker compose up`.

4. Запустите  `TaskManagementSystemApplication`.

После запуска документацию по API можно увидеть в [Swagger](http://localhost:8080/swagger-ui/index.html).  
Для тестирования REST-API можно использовать [Postman](https://www.postman.com/downloads/).
Тестовое покрытие (86%) можно посмотреть перейдя в `build\jacocoHtml` и запустив `index.html`.

## Контакты
Если у вас есть вопросы, вы можете связаться со мной в telegram - @oksana_buivarenko 
