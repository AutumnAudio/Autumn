export function joinRoom(username, genre) {
    let formData = new FormData()
    formData.append('username', username)
    return fetch('/joinroom/' + genre, {
        method: 'POST',
        body: formData
    })
}
export function getChatrooms() {
    return fetch('/chatrooms')
}
