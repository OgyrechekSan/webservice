<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Редактирование пользователя ${targetUser.name}</h1>
    <hr>

    <!-- Форма редактирования -->
    <form action="/admin/user/edit" method="post" class="mb-4">
        <#list roles as role>
            <div class="form-check mb-2">
                <input class="form-check-input" type="checkbox"
                       name="${role}" id="role-${role}"
                        ${targetUser.roles?seq_contains(role)?string("checked", "")}>
                <label class="form-check-label" for="role-${role}">
                    ${role}
                </label>
            </div>
        </#list>

        <input type="hidden" value="${targetUser.id}" name="userId">
        <input type="hidden" value="${_csrf.token}" name="_csrf">

        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary">Сохранить</button>
            <a href="/admin/users" class="btn btn-secondary">Отмена</a>
        </div>
    </form>

    <!-- Кнопка удаления с подтверждением -->
    <form action="/admin/user/delete/${targetUser.id}" method="post"
          onsubmit="return confirm('Вы точно хотите удалить пользователя ${targetUser.name}?')">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" class="btn btn-danger mt-3">
            Удалить пользователя
        </button>
    </form>
</@t.template>