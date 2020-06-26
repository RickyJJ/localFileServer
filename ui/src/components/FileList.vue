<template>
    <div class="mainContainer">
        <ul class="fileTree">
            <li v-if="!isRoot"><a href="javascript:void(0)" @click="getFileList('files/' + parentPath)">..</a></li>
            <li v-for="file of files" :key="file.name">
                <span class="file-icon" v-bind:style="{backgroundImage: imgUrl('static/icons/icon-file.png')}"></span>
                <a href="javascript:void(0)" @click="getFileList(fileHref(file))">{{file.name}}{{file.dir ? '&nbsp;->'
                    : ''}}</a>
                <div style="display: inline-block; float: right">
                    <span class="fileSize">{{file.size}}</span>
                    <small class="timestamp">{{file.lastModifiedDate}}</small>
                </div>
            </li>
        </ul>
    </div>
</template>

<script>
    export default {
        name: "FileList",
        data() {
            return {
                files: [],
                isRoot: false,
                parentPath: ''
            }
        },
        methods: {
            fileHref: function (file) {
                return file.dir ? ('files/' + file.path) : ('download/' + file.path)
            },
            imgUrl: function(imgPath) {
                return 'url(' + this.CONTEXT.path(imgPath) + ')';
            },
            getFileList(filePath) {
                let vm = this;
                filePath = filePath.replace(/\\/g, '/');
                if (filePath.indexOf("files/") == 0) {
                    this.$http.get(filePath).then(function (response) {
                        vm.isRoot = response.body.data.isRoot
                        vm.files = response.body.data.files
                        vm.parentPath = response.body.data.parentPath;
                    });
                } else if (filePath.indexOf("download/") == 0) {
                    window.location.href = this.CONTEXT.path(filePath);
                }
            }
        },
        created() {
            let vm = this
            this.$http.get('files').then(function (response) {
                vm.isRoot = response.body.data.isRoot
                vm.files = response.body.data.files

            });
        }
    }
</script>

<style scoped>
    .mainContainer {
        padding-bottom: 36px;
    }

    .fileTree {
        list-style: none;
        padding: 0 16px;
    }

    .fileTree > li {
        padding: 4px;
    }

    .fileSize {
    }

    .timestamp {
        margin-right: 16px;
    }

    .file-icon {
        width: 32px;
        height: 32px;
        display: inline-block;
        background-position: center;
        background-size: contain;
        background-repeat: no-repeat;
    }
</style>