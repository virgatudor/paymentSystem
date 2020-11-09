package net.javaguides.springboot.springsecurity.config;

import java.util.Collection;
 
import org.springframework.security.core.GrantedAuthority;

import net.javaguides.springboot.springsecurity.model.Role;
import net.javaguides.springboot.springsecurity.model.User;

 
public class WebUtils {
 
    public static String toString(User user) {
        StringBuilder sb = new StringBuilder();
 
        sb.append("UserName:").append(user.getFullName());
 
        Collection<Role> authorities = user.getRoles();
        if (authorities != null && !authorities.isEmpty()) {
            sb.append(" (");
            boolean first = true;
            for (Role a : authorities) {
                if (first) {
                    sb.append(a.getName());
                    first = false;
                } else {
                    sb.append(", ").append(a.getName());
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }
}