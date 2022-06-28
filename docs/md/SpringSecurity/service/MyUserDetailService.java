package deyo.xyz.securitylogin.service;

import deyo.xyz.securitylogin.entity.User;
import deyo.xyz.securitylogin.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Objects;

/**
 * @Author deyou
 * @Date 2022/6/13 16:02
 * @Version 1.0
 */
//@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername( username );
        if(Objects.isNull( user )){
            try {
                throw new AccountNotFoundException( "账户不存在"  );
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            }
        }
        user.setRoles( userMapper.getRolesByUid( user.getId() ) );
        return user;
    }
}
