package pProject.pPro.global;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import pProject.pPro.User.exception.UserErrorCode;
import pProject.pPro.User.exception.UserException;

@Component
public class ControllerUtils {
	public String findEmail(UserDetails user) {
		if(user==null) throw new UserException(UserErrorCode.REQUIRED_LOGIN);
		return user.getUsername();
	}
	public String findEmailOrNull(UserDetails user) {
		if(user==null) return null;
		return user.getUsername();
	}
	 public void isBannedUser(UserDetails user) {
	        boolean isBanned = user.getAuthorities().stream()
	            .anyMatch(auth -> auth.getAuthority().equals("ROLE_BANNED"));
	        
	        if (isBanned) {
	            throw new UserException(UserErrorCode.BANNED_USER);
	        }
	    }
}
