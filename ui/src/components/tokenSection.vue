<template>
    <div v-show="isShow">
        <div v-show="!hasToken">
            No available Token <a v-on:click="applyToken">Click to Apply!</a>
            <a v-on:click="testToken">Test token</a>
        </div>
        <div v-show="hasToken">{{token}}</div>
    </div>
</template>

<script>
    export default {
        name: "tokenSection",
        data() {
            return {
                isShow: true,
                hasToken: false,
                tokenType: '',
                tokenExpireDate: ''
            }
        },
        computed: {
            token: function () {
                if (this.tokenType == '1') return '长期';
                else return '有效期至' + this.tokenExpireDate;
            }
        },
        created() {
            this.$http.get('userToken').then(function (response) {
                if (response.body.flag == '1') {
                    this.hasToken = true;
                    this.tokenType = response.body.tokenType;
                    this.tokenExpireDate = response.body.tokenExpireDate;
                }
            });
        },
        methods: {
            applyToken() {
                this.$http.get('token/apply').then(function (response) {
                    if (response.body.flag == '1') {
                        alert('apply succeed.');
                    } else {
                        alert(response.body.msg);
                    }
                });
            },
            testToken() {
                this.$http.get('user/checkDownload').then(function (response) {
                    if (response.body.flag == 1) {

                        this.tokenType = 1;
                        this.hasToken = true;
                    } else {
                        alert('not has role');
                    }

                });
            }
        }
    }
</script>

<style scoped>

</style>