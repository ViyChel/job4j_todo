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
    if (validate(description)) {
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/todo/task.do',
            contentType: 'JSON',
            data: JSON.stringify({description: description})
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
        console.log(name)
        if (name === '') {
            $('#auth').append().html('<a class="nav-link" href="/todo/auth.do">Войти</a>')
        } else {
            $('#auth').append().html('<a class="nav-link" href="/todo/auth.do?exit=true">' + name + ' | Выйти</a>')
        }
    }).fail(function () {
        alert('An error occurred!')
    })
})

function validate(param) {
    if (param === '') {
        alert("Опишите задачу");
        return false;
    } else return true
}
