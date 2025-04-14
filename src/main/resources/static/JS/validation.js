function validation(form) {
    function removeError(input) {
        const parent = input.parentNode;

        if (parent.classList.contains('error')) {
            const errorLabels = parent.querySelectorAll('.error-label');
            errorLabels.forEach(label => label.remove());
            parent.classList.remove('error');
        }
    }

    function validateLogin(login) {
        const regex = /^[a-zA-Zа-яА-Я0-9_-]+$/;
        return regex.test(login);
    }

    function validateEmail(email) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }

    function validateName(name) {
        const regex = /^[A-ZА-Я][a-zа-я]*$/;
        return regex.test(name);
    }

    function createError(input, text) {
        const parent = input.parentNode;
        const errorLabel = document.createElement('label');

        errorLabel.classList.add('error-label');
        errorLabel.textContent = text;

        parent.classList.add('error');
        parent.append(errorLabel);
    }

    let result = true;

    const allInputs = form.querySelectorAll('input');

    for (const input of allInputs) {
        removeError(input);

        // Проверка обязательных полей
        if (input.dataset.required == 'true') {
            if (input.value == "") {
                createError(input, 'Поле не заполнено');
                result = false;
            }
        }

        // Проверка минимальной длины
        if (input.dataset.minLength) {
            if (input.value.length < input.dataset.minLength) {
                createError(input, `Минимальное кол-во символов: ${input.dataset.minLength}`);
                result = false;
            }
        }

        // Проверка максимальной длины
        if (input.dataset.maxLength) {
            if (input.value.length > input.dataset.maxLength) {
                createError(input, `Максимальное кол-во символов: ${input.dataset.maxLength}`);
                result = false;
            }
        }

        // Проверка email
        if (input.type === 'email' && input.value !== "") {
            if (!validateEmail(input.value)) {
                createError(input, 'Некорректный email');
                result = false;
            }
        }

        // Проверка имени и фамилии (только буквы, первая заглавная)
        if (input.id === 'name' || input.id === 'surname') {
            if (!validateName(input.value)) {
                createError(input, 'Только буквы, первая заглавная');
                result = false;
            }
        }

        if (input.id === 'login' && input.value !== "") {
            if (!validateLogin(input.value)) {
                createError(input, 'Логин содержит недопустимые символы');
                result = false;
            }
        }
    }

    return result;
}

document.getElementById('signUpForm').addEventListener('submit',function(event){
    event.preventDefault()

    if(validation(this)==true){
        alert('Форма проверена успешно')
    }
})

document.getElementById('loginForm').addEventListener('submit', function (event) {
    event.preventDefault();

    if (validation(this) == true) {
        alert('Форма входа проверена успешно');
    }
});