export default class Context {
    static serverContextPath = "http://192.168.0.100:8081/client/"

    static path(url) {
        return this.serverContextPath + url
    }
}