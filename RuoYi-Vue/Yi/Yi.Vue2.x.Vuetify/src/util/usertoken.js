const TOKEN_KEY = "token_key"
const USER_KEY = "user_key"
const PER_KEY="per_key"
export function getToken() {
    return localStorage.getItem(TOKEN_KEY)
}
export function getUser() {
    return JSON.parse(localStorage.getItem(USER_KEY))
}
export function getPer() {
    return JSON.parse(localStorage.getItem(PER_KEY))
}



export function setToken(token) {
    return localStorage.setItem(TOKEN_KEY, token)
}

export function setUser(user) {
    return localStorage.setItem(USER_KEY, JSON.stringify(user))
}
export function setPer(per) {
    return localStorage.setItem(PER_KEY, JSON.stringify(per))
}
export function removeToken() {
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
    localStorage.removeItem(PER_KEY)
}