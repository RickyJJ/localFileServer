package localfileserver.model;

import localfileserver.common.AppConst;
import localfileserver.kit.StringKit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.Jiong
 */
public class Result implements Serializable {

    private static final long serialVersionUID = -5842231463476197663L;

    private String flag;

    private String message;

    private Map<String, Object> data;

    public static Result fail(String flag, String message) {
        return fail(flag, message, null);
    }
    public static Result fail(String flag, String message, Map<String, Object> data) {
        Result result = new Result();
        result.flag = flag;
        result.message = StringKit.isEmpty(message) ? "" : message;
        result.data = data;
        return result;
    }

    public static Result ok(String message, Map<String, Object> data) {
        Result result = new Result();
        result.flag = AppConst.OK;
        result.message = StringKit.isEmpty(message) ? "" : message;
        result.data = data;
        return result;
    }

    public static Result ok() {
        return ok(null, null);
    }

    public static Result fail(String msg) {
        return fail(AppConst.FAIL, msg);
    }

    public Result add(String key, Object value) {
        if (data == null) {
            data = new HashMap<>();
        }

        data.put(key, value);
        return this;
    }

    public boolean isOk() {
        return AppConst.OK.equals(this.flag);
    }

    public boolean isFailed() {
        return !isOk();
    }

    public Object get(String key) {
        if (data != null) {
            return data.get(key);
        }

        return null;
    }

    public String getFlag() {
        return flag;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
