# Info

Учебная задача **Simple Instagram**.  
Проект состоит из репозиториев:  
- Backend (данный репозиторий)  
- [Frontend](https://github.com/aleksey-nsk/simple-insta-frontend)  

# Backend

1. Архитектура приложения:  
   ![](screenshots/01_arch.png)

2. Бэкенд реализован в виде **Spring Boot REST API**. Список реализованных **эндпоинтов**:    
   ![](screenshots/02_endpoints.png)
      
3. Для миграций используем **Liquibase**. Создаём следующую структуру таблиц:    
   ![](screenshots/03_tables.png)
   
4. Реализуем **регистрацию** новых пользователей и **аутентификацию** с помощью **Spring Security**   
   и **JWT-токенов**.
   
При успешной аутентификации объект типа **Authentication** сохраняется в **SecurityContext**, а тот в свою
очередь - в **SecurityContextHolder**:    
![](screenshots/04_spring_security.png)  

Текущего пользователя из SecurityContextHolder можно получить так:  

    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();
    UserDetails principal = (UserDetails) authentication.getPrincipal();

таким образом SecurityContext используется для хранения объекта Authentication.

Здесь:  
- `Principal` - содержит инфу про юзера. 
- `Credentials` - содержит пароль.
- `Authorities` - содержит права (`ROLE_USER`, и т.д.)

Для работы с JWT-токенами используем **библиотеку JJWT** (Java JWT: JSON Web Token for Java and Android).
Схема работы с библиотекой такая:  
![](screenshots/05_jwt.png)  

Если декодировать полученный токен на сайте https://jwt.io/, то в нём видны **клеймы**:  
- `id`;  
- `username`;   
![](screenshots/06_decode.png)  
  
5. Полученный backend тестируем с помощью **Postman**:  
   ![](screenshots/07_postman.png)  
