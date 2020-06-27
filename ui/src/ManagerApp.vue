<template>
    <div id="app">
        <div class="title"><h1>Tokens Request Presentation</h1></div>
        <TokenRequestList/>
    </div>
</template>

<script>

    import TokenRequestList from "./components/TokenRequestList";

    export default {
        name: 'ManagerApp',
        components: {
            TokenRequestList
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
        background-color: #fbffd4;
    }

    .title {
        text-align: center;
    }
</style>
