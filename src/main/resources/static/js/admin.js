$(() => {
    $.getJSON('/admin/rest', (users) =>{
        users.forEach(user => {buildTable(user)})
    })

    let editBtn = $(document).on('click', 'td#edit button', (btn) => {
        $('div#modalWin select').prop('disabled', false)
        $('div#modalWin option').prop('selected', false)
        let input = $('div#modalWin input')
        input.not('#id').prop('disabled', false)
        input.filter(`#password, #passwordConfirm`).parent().parent().show()
        input.filter('.btn-danger').attr('value', 'Edit')
        input.filter('.btn-danger').attr('class', 'btn btn-primary');
        $.getJSON(`/admin/rest/${btn.target.id}`, user => {
            for (let key in user){
                if(user.hasOwnProperty(key)) {
                    input.filter(`#${key}`).val(user[key])
                }
            }
            $('#modalForm input:submit').off().on('click', btnIn => {
                btnIn.preventDefault()
                for (let key in user){
                    if(user.hasOwnProperty(key)) {
                        user[key] = input.filter(`#${key}`).val()
                    }
                }
                let selectVal = $('div#modalWin select').val()[0]
                if (selectVal === '') {
                    user['confirm'] = null
                } else if (selectVal === 'ADMIN') {
                    user['confirm'] = selectVal
                }
                $.ajax({
                    url: `/admin/rest/${user.id}`,
                    contentType: "application/json; charset=utf-8",
                    method: 'patch',
                    dataType: 'json',
                    data: JSON.stringify(user),
                    success: function(data){
                        $('.modal').modal('hide')
                        refactorTable(data, 'edit')
                        $.getJSON('/rest/authorized', userAut => {
                            let navSpan = $('span.navbar-brand')
                            if (user.hasOwnProperty('userEmailWithRoles') && navSpan.text() !== userAut.userEmailWithRoles) {
                                navSpan.text(userAut.userEmailWithRoles)
                            }
                        })
                    }
                })

            })
        })
    })
    let deleteBtn = $(document).on('click', 'td#delete button', (btn) => {
        let input = $('div#modalWin input')
        $('div#modalWin select').prop('disabled', true)
        $('div#modalWin option').prop('selected', false)
        input.not(':submit').prop('disabled', true)
        input.filter(`#password, #passwordConfirm`).parent().parent().hide()
        input.filter('.btn-primary').attr('value', 'Delete')
        input.filter('.btn-primary').attr('class', 'btn btn-danger')
        $.getJSON(`/admin/rest/${btn.target.id}`, user => {
            for (let key in user){
                if(user.hasOwnProperty(key)) {
                    input.filter(`#${key}`).val(user[key])
                }
            }
            $('#modalForm input:submit').off().on('click', btnIn => {
                btnIn.preventDefault()
                $.ajax({
                    url: `/admin/rest/${user.id}`,
                    contentType: "application/json; charset=utf-8",
                    method: 'delete',
                    success: function(data){
                        $('.modal').modal('hide')
                        refactorTable(user, 'del')
                    }
                })
            })
        })
    })
    let registrationBtn = $(document).on('click', 'form#registForm input:submit', (btnReg) => {
        btnReg.preventDefault()
        let newUser = {}
        $('#registForm').find ('input').each(function() {
            if ($(this).val() !== 'Add new user') {
                newUser[this.name.replace('Reg', '')] = $(this).val()
            }
        })
        let selectVal = $('#registForm select').val()[0]
        if (selectVal === '') {
            newUser['confirm'] = null
        } else if (selectVal === 'ADMIN') {
            newUser['confirm'] = selectVal
        }
        $.ajax({
            url: '/admin/rest',
            method: 'post',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(newUser),
            success: createdUser => {
                buildTable(createdUser)
            },
            error: jqXHR => {
                console.log(jqXHR)
            }
        })
    })
    let refactorTable = (user, action) => {
        if (action === 'del') {
            $(`tr#tr${user.id}`).detach()
        } else if (action === 'edit') {
            let trUser = document.querySelector(`tbody #tr${user.id}`)
            trUser.innerHTML = `<td>${user.id}</td>
                                                <td>${user.firstName}</td>
                                                <td>${user.lastName}</td>
                                                <td>${user.age}</td>
                                                <td>${user.email}</td>
                                                <td>${user.username}</td>
                                                <td>${user.rolesString}</td>
                                                <td id="edit">
                                                    <div>
                                                        <button id="${user.id}" type="button" class="btn btn-secondary"
                                                                data-bs-toggle="modal"
                                                                data-bs-target="#modalWin"
                                                                >
                                                            Edit
                                                        </button>
                                                    </div>
                                                </td>
                                                <td id="delete">
                                                    <div>
                                                        <button id="${user.id}" type="button" class="btn btn-danger"
                                                                data-bs-toggle="modal"
                                                                data-bs-target="#modalWin"
                                                                >
                                                            Delete
                                                        </button>
                                                    </div>
                                                </td>`
        }
    }
    let buildTable = (user) => {
        let bodyTable = document.querySelector(`tbody`)
        bodyTable.innerHTML += `<tr id="tr${user.id}">
                                                <td>${user.id}</td>
                                                <td>${user.firstName}</td>
                                                <td>${user.lastName}</td>
                                                <td>${user.age}</td>
                                                <td>${user.email}</td>
                                                <td>${user.username}</td>
                                                <td>${user.rolesString}</td>
                                                <td id="edit">
                                                    <div>
                                                        <button id="${user.id}" type="button" class="btn btn-secondary"
                                                                data-bs-toggle="modal"
                                                                data-bs-target="#modalWin"
                                                                >
                                                            Edit
                                                        </button>
                                                    </div>
                                                </td>
                                                <td id="delete">
                                                    <div>
                                                        <button id="${user.id}" type="button" class="btn btn-danger"
                                                                data-bs-toggle="modal"
                                                                data-bs-target="#modalWin"
                                                                >
                                                            Delete
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>`
    }
})
