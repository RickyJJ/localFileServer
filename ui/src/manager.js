import Vue from 'vue'
import Resource from 'vue-resource'
import Context from "./config/Context";
import ManagerApp from "./ManagerApp";

Vue.config.productionTip = false
Vue.prototype.CONTEXT = Context

Vue.use(Resource)
Vue.http.interceptors.push(function (request, next) {
  request.url = this.CONTEXT.path(request.url)
  request.withCredentials = true
  next(function (response) {
    return response
  })
})

new Vue({
  render: h => h(ManagerApp),
}).$mount('#app')
