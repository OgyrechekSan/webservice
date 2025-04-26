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
            <button class="btn btn-outline-success like-btn" onclick="rateUser('LIKE')">
                <i class="fas fa-thumbs-up"></i>
                <span class="like-count">${ratingStats.likes!0}</span>
            </button>
            <button class="btn btn-outline-danger dislike-btn" onclick="rateUser('DISLIKE')">
                <i class="fas fa-thumbs-down"></i>
                <span class="dislike-count">${ratingStats.dislikes!0}</span>
            </button>
        </div>

        <#if userByPrincipal.isAdmin() || userByPrincipal.id == user.id>
            <div class="mt-4">
                <form action="/user/delete/${user.id}" method="post" onsubmit="return confirm('Вы точно хотите удалить этот аккаунт?')">
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

    <button class="btn btn-outline-success" onclick="rateUser('LIKE')">
        <i class="fas fa-thumbs-up"></i>
        <span id="likeCount">${ratingStats.likes!0}</span>
    </button>
    <button class="btn btn-outline-danger" onclick="rateUser('DISLIKE')">
        <i class="fas fa-thumbs-down"></i>
        <span id="dislikeCount">${ratingStats.dislikes!0}</span>
    </button>

    <!-- JavaScript для отправки лайков/дизлайков и обновления в реальном времени -->
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.6.1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>

    <script>
        const sellerId = ${sellerId!0};
        const currentUserId = ${(userByPrincipal.id)!0};

        // Инициализация WebSocket
        const socket = new SockJS('/ws');
        const stompClient = Stomp.over(socket);

        // Объявляем функцию глобально, чтобы она была доступна в onclick
        window.rateUser = function(type) {
            if (!stompClient.connected) {
                alert("Подождите, соединение устанавливается...");
                return;
            }

            stompClient.send("/app/rate", {}, JSON.stringify({
                userId: currentUserId,
                sellerId: sellerId,
                type: type
            }));
        };

        // Подключаемся к WebSocket
        stompClient.connect({}, function() {
            console.log("WebSocket подключен!");

            stompClient.subscribe('/topic/ratings/' + sellerId, function(message) {
                const stats = JSON.parse(message.body);
                document.getElementById('likeCount').textContent = stats.likes;
                document.getElementById('dislikeCount').textContent = stats.dislikes;
            });
        });
    </script>

</@t.template>