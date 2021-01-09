(() => setTimeout(print, 0))();

function print() {
    if (document.getElementById('checkDone').checked) {
        printTasks(true);
    } else {
        printTasks(false);
    }
}

function isDone(id) {
    let checkId = document.getElementById(id);
    if (checkId.checked) {
        setStatusTask(id, true);
    } else {
        setStatusTask(id, false);
    }
}

function setStatusTask(id, status) {
    let query = {id: id, status: status}
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/todo/task.do',
        contentType: 'JSON',
        data: JSON.stringify(query)
    }).done(function () {
        $('#tasks').after().html('')
        print()
    }).fail(function () {
        alert('An error occurred!')
    })
}

function printTasks(state) {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/todo/task.do',
        dataType: 'JSON',
    }).done(function (data) {
        if (data !== null) {
            let textRow = $('#tasks').after().html('');
            let text = '';
            for (let i = 0; i < data.length; i++) {
                let id = data[i].id;
                let description = data[i].description;
                let categories = data[i].categories;
                let created = data[i].created;
                let checked = '';
                if (state === true) {
                    if (data[i].done) {
                        checked = 'checked';
                    }
                    printRow();
                } else {
                    if (data[i].done) {
                        continue;
                    }
                    printRow();
                }

                function printRow() {
                    text += '<tr><td>' + description + '</td>';
                    text += '<td>' + categories + '</td>';
                    text += '<td>' + created + '</td>';
                    text += '<td>' + data[i].user + '</td>';
                    text += '<td><input class="ml-4" type="checkbox" id="' + id + '" value="" ' + checked + ' onclick="isDone(' + id + ')"></td></tr>';
                }
            }
            textRow.append(text);
        }
    }).fail(function () {
        alert('An error occurred!');
    })
}

function send() {
    sendTask(function (redirect) {
        if (redirect !== undefined) {
            window.location.href = redirect;
        } else {
            print()
        }
    })
}

function sendTask(fn) {
    let description = $('#textarea').val();
    let category = $('#categories').val();
    let task = {
        description: description,
        category: category
    }
    if (validate(description, category)) {
        console.log('!!!')
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/todo/task.do',
            contentType: 'JSON',
            data: JSON.stringify(task)
        }).done(function (data) {
            fn(data);
        }).fail(function () {
            alert('An error occurred while sending!');
        })
    }
}

$(document).ready(function () {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/todo/user.do',
    }).done(function (name) {
        if (name === '') {
            $('#auth').append().html('<a class="nav-link" href="/todo/auth.do">Войти</a>')
        } else {
            $('#auth').append().html('<a class="nav-link" href="/todo/auth.do?exit=true">' + name + ' | Выйти</a>')
        }
    }).fail(function () {
        alert('An error occurred!')
    })
})

$(document).ready(function printCategories() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/todo/categories.do',
        dataType: 'JSON'
    }).done(function (data) {
        if (data !== null) {
            let textRow = $('#categories').html('');
            let text = '';
            for (let i = 0; i < data.length; i++) {
                let id = data[i].id;
                let name = data[i].name;
                text += '<option value="' + id + '">' + name + '</option>';
            }
            textRow.append(text);
        }
    }).fail(function () {
        alert('An error occurred!');
    })
})


function validate(desc, category) {
    if (desc === '') {
        alert("Опишите задачу");
        return false;
    } else if (category.length === 0) {
        alert("Выберите категорию");
        return false;
    }
    return true;
}
