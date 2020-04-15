package org.jiong.filetree.service.impl;

import org.jiong.filetree.model.Result;
import org.jiong.filetree.service.ServerService;
import org.jiong.protobuf.TokenInfo;
import org.springframework.stereotype.Service;

/**
 * impl for IServerService
 * @author Mr.Jiong
 */
@Service
public class ServerServiceImpl implements ServerService {
    @Override
    public Result applyToken(String ipAddr, String userName) {
        return null;
    }

    @Override
    public Result addToResultQueue(String ipAddr, String userName, String key, TokenInfo.Token token) {
        return null;
    }

    @Override
    public Result findFromResultQueue(String ipAddr, String userName, String key) {
        return null;
    }
}
