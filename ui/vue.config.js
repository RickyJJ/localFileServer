module.exports = {
    outputDir: "dst",
    pages: {
        client: {
            entry: "src/main.js",
            template: "public/index.html",
            filename: "client.html",
            title: "Client Page"
        },
        manager: {
            entry: "src/manager.js",
            template: "public/index.html",
            filename: "manager.html",
            title: "Manager Page"
        }
    }
}