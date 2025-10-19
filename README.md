# Marketplace Web Application

Full-stack веб-приложение маркетплейса с полным циклом разработки - от проектирования до продакшн-деплоя.

*   [Java](https://www.java.com/) - Основной язык программирования
*   [Spring Framework](https://spring.io/) - Веб-фреймворк
*   [Docker](https://www.docker.com) - контейнеризация
*   [PostgreSQL](https://www.postgresql.org/) - База данных
*   [GitHub Actions](https://github.com/features/actions) - CI/CD
*   [Render](https://render.com) - хостинг

## О проекте

Веб-сервис для размещения объявлений о продаже товаров с системой пользователей, рейтингами и административной панелью. Проект реализован как полнофункциональное full-stack приложение.

**Основные возможности:**
- Регистрация и аутентификация пользователей
- Создание и управление товарами
- Загрузка изображений товаров
- Система рейтингов продавцов
- Обновления в реальном времени через WebSocket
- Административная панель
- Поиск товаров

## Технологический стек

### Backend
- **Java 21** - основной язык программирования
- **Spring Boot 3** - фреймворк
  - Spring MVC
  - Spring Security
  - Spring Data JPA
  - Spring WebSocket
- **Maven** - управление зависимостями

### Frontend
- **FreeMarker** - шаблонизатор
- **Bootstrap 5** - CSS фреймворк
- **JavaScript** - клиентская логика
- **WebSocket (STOMP)** - реальное время

### База данных
- **PostgreSQL** - основная БД
- **Hibernate/JPA** - ORM

### DevOps
- **Docker** - контейнеризация
- **GitHub Actions** - CI/CD
- **Render.com** - хостинг
