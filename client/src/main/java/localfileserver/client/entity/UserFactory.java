package localfileserver.client.entity;

import localfileserver.client.config.DirectoryProperties;
import localfileserver.entity.TokenEntity;
import localfileserver.kit.TokenKit;
import localfileserver.protobuf.TokenInfo;
import localfileserver.protobuf.UserInfo;
import localfileserver.token.ExpireHandleTokenWrapper;
import localfileserver.token.HandleToken;
import localfileserver.token.HandleTokenWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Mr.Jiong
 */
@Slf4j
@Component
public class UserFactory {

    private static DirectoryProperties directoryProperties;

    private UserFactory(DirectoryProperties directoryProperties) {
        UserFactory.directoryProperties = directoryProperties;
    }

    private static final String USER_FILE_SUFFIX = ".data";

    /**
     * 从本地加载用户信息，一定返回非NULL，否则抛出异常
     * @param uniqueKey 表明用户唯一性的标识，一般是IP
     * @return user
     */
    public static User load(String uniqueKey) {
        String userFile = TokenKit.encode(uniqueKey);
        File file = new File(directoryProperties.getUserPath() + File.separator + userFile + USER_FILE_SUFFIX);

        boolean userNew = false;
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
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
        user.setInfoPath(file.getAbsolutePath());

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
        TokenEntity tokenEntity = TokenEntity.toEntity(token);
        TokenInfo.Token.TokenType type = token.getType();

        if (type == TokenInfo.Token.TokenType.FOREVER) {
            return HandleTokenWrapper.toHandleToken(tokenEntity);

        } else if (type == TokenInfo.Token.TokenType.TEMP) {
            return ExpireHandleTokenWrapper.toHandleToken(tokenEntity);

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
