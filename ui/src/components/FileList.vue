<template>
    <div class="mainContainer">
        <ul class="fileTree">
            <li v-if="!isRoot"><a v-bind:href="this.CONTEXT.path('/files/#(parentPath)')">..</a></li>
            <template v-else>
                <li v-for="file of files" :key="file.name">
                    <a v-bind:href="fileHref(file)">{{file.name}}{{file.isDir ? '&nbsp;->' : ''}}</a>
                    <div style="display: inline-block; float: right">
                        <span class="fileSize">{{file.size}}</span>
                        <small class="timestamp">{{file.lastModifiedDate}}</small>
                    </div>
                </li>
            </template>
        </ul>
    </div>
</template>

<script>
    export default {
        name: "FileList",
        data() {
            return {
                files: [],
                isRoot: false
            }
        },
        methods: {
            fileHref: function (file) {
                let s = file.dir ? ('files/' + file.name) : ('download/' + file.path);
                return this.CONTEXT.path(s)
            },
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
</style>