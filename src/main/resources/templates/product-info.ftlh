<#import "blocks/template.ftlh" as t>
<@t.template user>
    <div id="myCarousel" class="carousel slide" data-ride="carousel" style="max-width: 100%; margin: 0 auto; background-color: dimgray;">
        <!-- Индикаторы -->
        <ol class="carousel-indicators">
            <#list images as image>
                <li data-target="#myCarousel" data-slide-to="${image?index}" <#if image?is_first>class="active"</#if>></li>
            </#list>
        </ol>

        <!-- Слайды -->
        <div class="carousel-inner">
            <#list images as image>
                <div class="carousel-item <#if image?is_first>active</#if>">
                    <!-- Убрали SVG, оставили только img -->
                    <img class="d-block w-100" src="/images/${image.id}"
                         style="max-height: 500px; width: auto; object-fit: contain; margin: 0 auto;"
                         alt="Slide ${image?index + 1}">
                </div>
            </#list>
        </div>

        <!-- Кнопки навигации -->
        <a class="carousel-control-prev" href="#myCarousel" role="button" data-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="carousel-control-next" href="#myCarousel" role="button" data-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>
    <hr>
    <p><h1 style="text-align: center">${product.title} | ${product.price} ₽</h1></p>
    <p style="text-align: center">${product.description}</p>
    <p style="text-align: center">Номер телефона продавца: <b>${authorProduct.phoneNumber}</b></p>

    <hr>
    <p style="text-align: center">
        <a href="/user/${authorProduct.id}">
            <img src="/static/images/avatar.png" alt="mdo" width="32" height="32" class="rounded-circle"> ${authorProduct.name}
        </a>
    </p>

</@t.template>