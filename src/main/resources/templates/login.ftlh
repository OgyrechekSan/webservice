<!DOCTYPE html>
<html>
<head>
    <title>Webservice</title>
</head>
<body>
<h1>Webservice</h1><hr>
<h4>Авторизация</h4>
<form action="/login" method="post">
    Эл. почта: <input type="email" name="username"><br><br>
    Пароль: <input type="password" name="password"><br><br>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Вход"/>
    <#if RequestParameters.error??>
        <div style="color: red;">Неверный email или пароль</div>
    </#if>
</form>
<a href="/registration">Зарегистрироваться</a>
</body>
</html>