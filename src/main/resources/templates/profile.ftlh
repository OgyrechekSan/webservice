<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Личный кабинет</h1>
    <hr>
    <br>
    <div style="text-align: center">
        <img src="/static/images/avatar.png"
             style="border:2px solid black; border-radius: 20px" height="120">
        <br>
        <br>
        <h2>${user.name}</h2>
        <br>
        Электоронная почта: <b>${user.email}</b>
        <br>
        Номер телефона: <b>${user.phoneNumber}</b>
        <br>
        <br>
        <#if user.isAdmin()>
            <a class="btn btn-dark mb-3" style="text-align: center; width: 100%"
               href="/admin">
                Панель администратора
            </a><br>
        </#if>
        <a class="btn btn-dark mb-3" style="text-align: center; width: 100%"
           href="/my/products">
            Мои товары
        </a>
        <form action="/logout" method="post" class="mb-2">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit" class="btn btn-secondary">Выйти из аккаунта</button>
        </form>

        <form action="/user/delete/${user.id}" method="post"
              onsubmit="return confirm('ВНИМАНИЕ! Все ваши товары будут удалены. Вы уверены?')">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit" class="btn btn-danger">Удалить аккаунт</button>
        </form>
    </div>
</@t.template>