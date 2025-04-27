<#import "blocks/template.ftlh" as t>
<@t.template userByPrincipal>
    <h1 style="text-align: center">Информация о пользователе</h1>
    <hr>
    <div style="text-align: center">
        <img src="/static/images/avatar.png"
             style="border:2px solid black; border-radius: 20px" height="120">
        <br>
        <br>
        <h2>${user.name}</h2>
        <br>
        Номер телефона: <b>${user.phoneNumber}</b>

        <div class="rating-container mt-3">
            <button class="btn btn-outline-success like-btn" onclick="sendAction('LIKE')">
                <i class="fas fa-thumbs-up"></i>
                <span id="likeCount">${ratingStats.likes!0}</span>
            </button>
            <button class="btn btn-outline-danger dislike-btn" onclick="sendAction('DISLIKE')">
                <i class="fas fa-thumbs-down"></i>
                <span id="dislikeCount">${ratingStats.dislikes!0}</span>
            </button>
        </div>


        <#if userByPrincipal.isAdmin() || userByPrincipal.id == user.id>
            <div class="mt-4">
                <form action="/user/delete/${user.id}" method="post" onsubmit="
                return confirm('Вы точно хотите удалить этот аккаунт?')">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <button type="submit" class="btn btn-danger">
                        <i class="fas fa-trash-alt"></i> Удалить аккаунт
                    </button>
                </form>
            </div>
        </#if>
    </div>
    <br>
    <hr>
    <h1 style="text-align: center">Товары пользователя ${user.name}</h1>
    <br>
    <div class="card-columns">
        <#list products as product>
            <a href="/product/${product.id}" class="product-link">
                <div class="card text-white bg-dark my-3 product">
                    <img src="/images/${product.previewImageId}"
                         class="card-img-top">
                    <div class="card-body">
                        <h5 class="card-title">${product.title}</h5>
                        <p class="card-text">${product.price} ₽</p>
                    </div>
                    <div class="card-footer">
                        ${product.user.name}
                    </div>
                </div>
            </a>
        <#else>
            <h1 style="text-align: center">Не найдено</h1>
        </#list>
    </div>


    <!-- Обязательно подключаем FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <!-- Подключение SockJS и StompJS -->
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>

    <script>
        // Берем sellerId и userId из модели
        const sellerId = "${sellerId}";
        const currentUserId = "${userByPrincipal.id}";

        // Инициализация WebSocket
        const socket = new SockJS('/ws');
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, () => {
            console.log('Connected to WebSocket');

            // Подписка на канал продавца
            stompClient.subscribe(`/topic/ratings/${sellerId}`, (message) => {
                const data = JSON.parse(message.body);
                document.getElementById('likeCount').textContent = data.likes;
                document.getElementById('dislikeCount').textContent = data.dislikes;
            });
        });

        // Функция отправки лайка или дизлайка
        function sendAction(type) {
            const message = {
                userId: currentUserId,
                sellerId: sellerId,
                type: type
            };
            stompClient.send("/app/rate", {}, JSON.stringify(message));
        }
    </script>

</@t.template>