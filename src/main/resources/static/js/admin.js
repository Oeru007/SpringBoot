let editBtn = $('td#edit button').on('click', () => {
    let userId = editBtn.attr('id')
    $.getJSON( `admin/rest/${userId}`, function (user) {
        console.log(user)
    })
})