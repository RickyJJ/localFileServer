package localfileserver.client.config.auth;

import localfileserver.client.entity.UserFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/***
 *
 * @author Administrator
 * @date 2020/5/20
 */
@Slf4j
@Configuration
public class UserDetailInfoImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String remoteAddress) throws UsernameNotFoundException {
        log.info("loading user details, user address: {}", remoteAddress);

        localfileserver.client.entity.User user = UserFactory.load(remoteAddress);
        return User.builder().authorities(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).username(user.getName()).password("2").build();
    }
}
