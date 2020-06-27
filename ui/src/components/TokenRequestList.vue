<template>
    <div class="mainContainer">
        <div class="token-request-item" v-for="(item, index) in items" v-bind:key="item.key + index">
            <div class="token-request-title">
                <p>{{item.userName}}</p>
                <span>{{item.ip}}</span>
            </div>
            <div class="token-request-handle" v-if="item.status == '0'">
                <a class="token-handle-btn" v-on:click="approveToken('1', item, index)">Forever</a>
                <a class="token-handle-btn" v-on:click="approveToken('0', item, index)">Temporal</a>
                <a class="token-handle-btn" v-on:click="approveToken('2', item, index)">Reject</a></div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "TokenRequestList",
        data() {
            return {
                items: []
            }
        },
        created() {
            this.$http.get('tokenRequests').then(function (response) {
                if (response.body.flag == '1') {
                    this.items = response.body.data.list
                }
            });
        },
        methods: {
            approveToken(tokenType, item, index) {
                this.$http.post('token/dispatch', {userName: item.userName, tokenType: tokenType, key: item.key})
                    .then(function (response) {
                        if (response.body.flag == '1') {
                            this.$delete(this.items, index)
                        }else {
                            alert(response.body.msg)
                        }
                    });
            }
        }
    }
</script>

<style scoped>

    .token-request-item {
        border: 1px solid #effffe;
        box-shadow: #4f2b33 0 5px 40px 8px;
        border-radius: 8px;
        padding: 0 16px;
    }

    .token-request-title {
        display: inline-block;
    }

    .token-request-handle {
        float: right;
        margin: 16px 0;
    }

    .token-handle-btn {
        font-size: 0.8rem;
        color: #595959;
        cursor: pointer;
        margin: 0 4px;
    }

    .token-handle-btn:hover {
        text-decoration: underline #494949;
    }
</style>