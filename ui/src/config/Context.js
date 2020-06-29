export default class Context {
    static serverContextPath = "http://http://www.jiong-story.online:8081/client/"

    static path(url) {
        return this.serverContextPath + url
    }
}