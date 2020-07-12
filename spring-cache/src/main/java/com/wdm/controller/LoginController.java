package com.wdm.controller;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wdm.controller.vo.Response;
import com.wdm.controller.vo.UserView;
import com.wdm.enums.ClientType;
import com.wdm.enums.Code;
import com.wdm.model.User;
import com.wdm.service.UserService;
import com.wdm.utils.LoginUserIdContextHolder;
import com.wdm.utils.TokenUtils;

/*
 * @author wdmyong
 * 2020-07-09
 */
@RestController
@RequestMapping("")
public class LoginController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    // 返回token
    @ResponseBody
    @RequestMapping(value = "/app/login", method = RequestMethod.POST)
    public Response appLogin(String account, String password) {
        return login(account, password, ClientType.APP, null);
    }

    // 种cookie
    @ResponseBody
    @RequestMapping(value = "/web/login", method = RequestMethod.POST)
    public Response webLogin(String account, String password, HttpServletResponse httpServletResponse) {
        return login(account, password, ClientType.WEB, httpServletResponse);
    }

    @ResponseBody
    @RequestMapping(value = "/userInfo", method = RequestMethod.POST)
    public Response userInfo() {
        Response result = success();
        long userId = LoginUserIdContextHolder.getUserId();
        if (userId <= 0) {
            logger.info("illegal login user");
            return result;
        }
        User user = userService.getById(userId);
        result.setData(geneData("user", UserView.of(user)));
        return result;
    }

    private Response login(String account, String password, ClientType clientType, HttpServletResponse httpServletResponse) {
        Response result = success();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return errorResponse(Code.PARAM_INVALID);
        }

        User user = userService.getByAccountPassword(account, password);
        if (user == null) {
            return errorResponse(Code.ACCOUNT_OR_PASSWORD_INVALID);
        }

        String token = TokenUtils.generateToken(user);
        Map<String, Object> data = geneData("token", token);
        result.setData(data);

        if (clientType == ClientType.WEB) {
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            cookie.setDomain("user.test.com");
            cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(1));
            httpServletResponse.addCookie(cookie);
        }
        return result;
    }
}
