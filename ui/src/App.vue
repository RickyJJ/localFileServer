<template>
    <div id="app">
        <div class="title"><h1>Files Presentation</h1></div>
        <FileList/>
        <div class="fix-bottom" style="text-align: center">
            <form v-bind:action="this.CONTEXT.path('fileUpload')" method="post" enctype="multipart/form-data">
                <div>
                    <div class="btn-upload">Click to Upload File(Drag file to here)
                        <input type="file" class="file-hidden" name="file" v-on:change="submitFile()">
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>

<script>
    import FileList from './components/FileList'

    export default {
        name: 'App',
        components: {
            FileList
        },
        methods: {
            submitFile () {
                let form = document.forms[0];
                let filePath = form.file.value;
                if (filePath && filePath.length > 0) {
                    if (confirm("Upload This file?")) {
                        form.submit();
                    }
                    form.reset();
                }
            }
        }
    }
</script>

<style>
    #app {
        font-family: Avenir, Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        color: #2c3e50;
    }

    .bg {
        background-color: #b7ffd9;
    }

    .title {
        text-align: center;
    }


    .fix-bottom {
        position: fixed;
        bottom: 0;
        left: 0;
        right: 0;
        margin: 0 auto 16px auto;
    }

    .btn-upload {
        outline-style: none;
        background-color: #89e88b;
        border-radius: 5rem;
        width: 75%;
        height: 36px;
        border: none;
        margin: 0 auto;
        text-align: center;
        line-height: 36px;
        position: relative;
    }

    .btn-upload:active {
        background-color: #6fb769;
    }

    .file-hidden {
        position: absolute;
        width: 100%;
        height: 100%;
        opacity: 0;
        top: 0;
        left: 0;
    }
</style>
