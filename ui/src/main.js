import Vue from 'vue'
import App from './App.vue'
import Resource from 'vue-resource'

Vue.config.productionTip = false

Vue.use(Resource)
Vue.http.interceptors.push(function (request, next) {
  request.url = "http://127.0.0.1:8080/client/" + request.url
  next(function (response) {
    return response
  })
})

new Vue({
  render: h => h(App),
}).$mount('#app')
