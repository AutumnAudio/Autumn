function addUserApi(username, password) {
    return fetch('http://localhost:8081/addUser');
}