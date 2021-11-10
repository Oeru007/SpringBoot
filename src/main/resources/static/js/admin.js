$(document).ready(() => {
    let users
    $.getJSON('/admin/rest', (data) =>{
        users = data
    })
})
let editBtnModal
let editBtn = $('td#edit button').on('click', (btn) => {
    let input = $('div#modalWin input')
    input.prop('disabled', false)
    input.filter(`#password, #passwordConfirm`).parent().parent().show()
    input.filter('.btn-danger').attr('value', 'Edit')
    input.filter('.btn-danger').attr('class', 'btn btn-primary');
    $.getJSON(`/admin/rest/${btn.target.id}`, user => {
        for (let key in user){
            if(user.hasOwnProperty(key)) {
                input.filter(`#${key}`).val(user[key])
            }
        }
        editBtnModal = $('div.modal-footer input.btn-primary').on('click', btnIn => {
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
                    console.log(data)
                }
            });
            console.log(user)
        })
    })

})
let deleteBtn = $('td#delete button').on('click', (btn) => {
    let input = $('div#modalWin input')
    input.not('.btn-primary').prop('disabled', true)
    input.filter(`#password, #passwordConfirm`).parent().parent().hide()
    input.filter('.btn-primary').attr('value', 'Delete')
    input.filter('.btn-primary').attr('class', 'btn btn-danger')
    $.getJSON(`/admin/rest/${btn.target.id}`, user => {
        console.log(user)
        for (let key in user){
            if(user.hasOwnProperty(key)) {
                input.filter(`#${key}`).val(user[key])
            }
        }
    })
})

