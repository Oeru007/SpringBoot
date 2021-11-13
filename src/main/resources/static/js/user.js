$(() => {
    $.getJSON('/rest/authorized', userAvt => {
        buildTable(userAvt)
    })
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
                                            </tr>`
    }
})