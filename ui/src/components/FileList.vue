<template>
    <div class="mainContainer">
        <ul class="fileTree">
            <li v-if="!isRoot">
                <span class="file-icon" v-bind:style="{transform: 'rotate(240deg)', backgroundImage: imgUrl('')}"></span>
                <a href="javascript:void(0)" class="file-link"
                                  @click="getFileList('files/' + parentPath)">..</a></li>
            <li v-for="file of files" :key="file.name">
                <span class="file-icon" v-bind:style="{backgroundImage: imgUrl(file.name, file.dir)}"></span>
                <a href="javascript:void(0)" class="file-link" @click="getFileList(fileHref(file))">{{file.name}}{{file.dir
                    ? '&nbsp;->'
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
            imgUrl: function (imgName, isDir) {
                var icon
                if (isDir) {
                    icon = "icons/icon-dir.png";
                } else if (imgName == '') {
                    icon = "icons/icon-to-parent.png";
                } else {
                    var namesSplits = imgName.split('.')
                    var suffix = namesSplits[namesSplits.length - 1];
                    icon = this.isSupportFileType(suffix) ? this.getIcon(suffix) : this.getIcon('file');
                }
                return 'url(' + this.CONTEXT.path('static/' + icon) + ')';
            },

            isSupportFileType(suffix) {
                var supportedFileType = "7Z,BAT,BIN,CONF,CSS,DATA,DIR,DOC,DOCX,EXE,FILE,HTML,ICO,INI,JAVA,JPEG,JPG,PDF,PNG,PPT,PSD,PY,RAR,SH,SVG,TAR,TTF,TXT,XLSX,XML,YML,ZIP";
                return supportedFileType.indexOf(suffix.toUpperCase()) > -1
            },
            getIcon(suffix) {
                return "icons/icon-" + suffix + '.png';
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

    .file-link {
        color: #494949;
        margin-left: 0.5rem;
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
        vertical-align: bottom;
    }
</style>