package org.jiong.filetree.service.impl;

import com.google.common.base.Strings;
import org.jiong.filetree.common.constants.AppConst;

import java.util.Map;

/***
 * providing base methods to give some convenience
 * @author Administrator
 * @date 2020/4/4 0004
 */
public class BaseService {
    protected boolean isSucc(String code) {
        return !Strings.isNullOrEmpty(code) && AppConst.OK.equals(code);
    }

    protected boolean isSucc(Map<String, Object> map) {
        return map != null && isSucc((String) map.get("code"));
    }

    protected boolean isFail(String code) {
        return Strings.isNullOrEmpty(code) || !AppConst.OK.equals(code);
    }
}
