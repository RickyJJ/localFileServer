export default class Context {
    static serverContextPath = "http://localhost:8081/client/"

    static path(url) {
        return this.serverContextPath + url
    }
}