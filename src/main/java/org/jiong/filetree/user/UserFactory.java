package org.jiong.filetree.user;

import lombok.extern.slf4j.Slf4j;
import org.jiong.filetree.common.DirectoryProperties;
import org.jiong.filetree.common.util.TokenKit;
import org.jiong.filetree.token.ExpireHandleTokenWrapper;
import org.jiong.filetree.token.HandleToken;
import org.jiong.filetree.token.HandleTokenWrapper;
import org.jiong.protobuf.TokenInfo;
import org.jiong.protobuf.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Mr.Jiong
 */
@Slf4j
public class UserFactory {

    @Autowired
    private static DirectoryProperties directoryProperties;

    private static final String USER_FILE_SUFFIX = ".data";

    public static User load(String uniqueKey) {
        String userFile = TokenKit.encode(uniqueKey);
        File file = new File(directoryProperties.getDir() + File.pathSeparator + userFile + USER_FILE_SUFFIX);

        boolean userNew = false;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                } catch (IOException ex) {
                    log.error("User file creating failed", e);
                    throw new IllegalStateException("Env is wrong, cant create user file");
                }
            }

            userNew = true;
        }

        User user = new User();
        user.setName(uniqueKey);
        user.setInfoPath(userFile);

        if (!userNew) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                UserInfo.User userProto = UserInfo.User.parseFrom(inputStream);

                final String id = userProto.getId();
                if (!uniqueKey.equals(id)) {
                    log.error("request unique key: {}, user id: {}", uniqueKey, id);
                    throw new IllegalArgumentException("User key identify failed");
                }

                user.setToken(readToken(userProto));
            } catch (IOException e) {
                // ignore
            }
        } else {
            // ignore
            final UserInfo.User newUserProto = createNewUserProto(file, uniqueKey);

        }
        return user;
    }

    private static HandleToken readToken(UserInfo.User userProto) {
        TokenInfo.Token token = userProto.getToken();
        TokenInfo.Token.TokenType type = token.getType();
        if (type == TokenInfo.Token.TokenType.FOREVER) {
            return HandleTokenWrapper.asHandleToken(token);
        } else if (type == TokenInfo.Token.TokenType.TEMP) {
            return ExpireHandleTokenWrapper.asHandleToken(token);
        } else {
            throw new IllegalArgumentException("Unknown token type.");
        }
    }

    private static UserInfo.User createNewUserProto(File file, String uniqueKey) {
        UserInfo.User.Builder userBuilder = UserInfo.User.newBuilder();
        userBuilder.setId(uniqueKey);
        userBuilder.setName(uniqueKey);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            final UserInfo.User user = userBuilder.build();
            user.writeTo(outputStream);
            return user;
        } catch (IOException e) {
            log.error("user file created failed", e);
            throw new IllegalArgumentException("File state is wrong.", e);
        }
    }


}
