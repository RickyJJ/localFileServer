<template>
    <div v-show="isShow" class="token-section">
        <div v-show="!hasToken">
            No available Token <a v-on:click="applyToken">Click to Apply!</a>
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
                if (this.tokenType == '1') return 'TOKEN: FOREVER';
                else return 'TOKEN DEAD-TIME: ' + this.tokenExpireDate;
            }
        },
        created() {
            this.$http.get('userToken').then(function (response) {
                if (response.body.flag == '1') {
                    this.hasToken = true;
                    this.tokenType = response.body.data.tokenType;
                    this.tokenExpireDate = response.body.data.tokenExpireDate;
                }
            });
        },
        methods: {
            applyToken() {
                this.$http.get('token/apply').then(function (response) {
                    if (response.body.flag == '1') {
                        if (response.body.data.hasToken == '1') {
                            this.tokenType = response.body.data.tokenType;
                            this.tokenExpireDate = response.body.data.tokenExpireDate;
                            this.hasToken = true;
                            return;
                        }
                        alert('apply succeed.');
                    } else {
                        alert(response.body.msg);
                    }
                });
            }
        }
    }
</script>

<style scoped>
    .token-section {
        margin-left: 1rem;
        display: inline-block;
        border: 1px solid #844f4f;
        border-radius: 8px;
        padding: 5px 8px;
    }
</style>