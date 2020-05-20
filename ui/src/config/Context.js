export default class Context {
    static serverContextPath = "http://127.0.0.1:8081/client/"

    static path(url) {
        return this.serverContextPath + url
    }
}