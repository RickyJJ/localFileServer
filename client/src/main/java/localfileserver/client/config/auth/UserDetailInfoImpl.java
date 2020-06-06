package localfileserver.client.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/***
 *
 * @author Administrator
 * @date 2020/5/20
 */
@Slf4j
@Configuration
public class UserDetailInfoImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("loading user details, username: {}", s);

        List<GrantedAuthority> authorityList = AuthorityUtils.NO_AUTHORITIES;
        return User.builder().authorities(authorityList).username(s).password("2").build();
    }
}
