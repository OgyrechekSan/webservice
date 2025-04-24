<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Поиск товаров</h1><hr>
    <form method="get" action="/">
        <div class="form-row">
            <div class="form-group col-md-2">
                <label for="searchCity">Город</label>
                <select id="searchCity" class="form-control">
                    <option value="">Город не выбран</option>
                    <option value="Москва">Москва</option>
                    <option value="Красноярск">Красноярск</option>
                    <option value="Казань">Казань</option>
                </select>
            </div>
            <div class="form-group col-md-10">
                <label for="searchWord">Поиск по объявлениям</label>
                <input type="text" name="searchWord" <#if searchWord??>value="${searchWord}"</#if> class="form-control"
                       id="searchWord">
            </div>
            <button style="width: 100%" type="submit" class="btn btn-dark">Поиск</button>
        </div>
    </form>
    <hr>
    <div class="card-columns">
        <#list products as product>
            <a href="/product/${product.id}" class="product-link" style="display: block; height: 100%;">
                <div class="card text-white bg-dark my-3 product" style="height: 400px; overflow: hidden;">
                    <div style="height: 200px; background: #777; display: flex; justify-content: center; align-items: center;">
                        <img src="/images/${product.previewImageId}"
                             class="card-img-top"
                             style="max-height: 100%; max-width: 100%; object-fit: contain;">
                    </div>
                    <div class="card-body" style="height: 150px; display: flex; flex-direction: column;">
                        <h5 class="card-title" style="flex-grow: 1;">${product.title}</h5>
                        <p class="card-text">${product.price} ₽</p>
                    </div>
                    <div class="card-footer">
                        ${product.city}
                    </div>
                </div>
            </a>
        <#else>
            <h1 style="text-align: center">Не найдено</h1>
        </#list>
    </div>
</@t.template>