package deyo.xyz.securitylogin.mapper;

import deyo.xyz.securitylogin.entity.Role;
import deyo.xyz.securitylogin.entity.User;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.ManagedBean;
import java.util.List;

/**
 * @Author deyou
 * @Date 2022/6/13 15:54
 * @Version 1.0
 */
//@Mapper
public interface UserMapper {
    List<Role> getRolesByUid(Integer id);
    User loadUserByUsername(String username);
}
