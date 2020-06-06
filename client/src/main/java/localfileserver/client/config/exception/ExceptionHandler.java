package localfileserver.client.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/***
 * 增加全局异常处理
 * @author Administrator
 * @date 2020/5/21
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseBody
    public Object globalExceptionHandler(HttpServletRequest request, Exception e, HandlerMethod method) {
        log.error("Caught Exception.", e);;
        if (isAjax(method)) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", "0");
            map.put("msg", e.getMessage());

            return map;
        } else {
            ModelAndView error = new ModelAndView("error");

            error.addObject("message", "Custom Wrong Page");
            error.addObject("error", "page error");
            error.addObject("status", "510");
            return error;
        }
    }

    private boolean isAjax(HandlerMethod method) {
        ResponseBody responseBodyAnnotation = method.getMethodAnnotation(ResponseBody.class);

        if (responseBodyAnnotation != null) {
            return true;
        }

        RestController restControllerAnnotation = method.getClass().getDeclaredAnnotation(RestController.class);
        return restControllerAnnotation != null;
    }

}
