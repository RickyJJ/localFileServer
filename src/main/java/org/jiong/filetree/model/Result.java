package org.jiong.filetree.model;

import com.google.common.base.Strings;
import org.jiong.filetree.common.constants.AppConst;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.Jiong
 */
public class Result {
    private String flag;

    private String message;

    private Map<String, Object> data;

    public static Result fail(String flag, String message) {
        return fail(flag, message, null);
    }
    public static Result fail(String flag, String message, Map<String, Object> data) {
        Result result = new Result();
        result.flag = flag;
        result.message = Strings.isNullOrEmpty(message) ? "" : message;
        result.data = data;
        return result;
    }

    public static Result ok(String message, Map<String, Object> data) {
        Result result = new Result();
        result.flag = AppConst.OK;
        result.message = Strings.isNullOrEmpty(message) ? "" : message;
        result.data = data;
        return result;
    }

    public static Result ok() {
        return ok(null, null);
    }

    public Result add(String key, String value) {
        if (data == null) {
            data = new HashMap<>();
        }

        data.put(key, value);
        return this;
    }
}
