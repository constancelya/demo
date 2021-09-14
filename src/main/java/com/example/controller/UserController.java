package com.example.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.common.lang.Result;
import com.example.entity.Post;
import com.example.entity.User;
import com.example.entity.UserMessage;
import com.example.service.UserService;
import com.example.shiro.AccountProfile;

import com.example.util.UploadUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class UserController extends BaseController{

    @Autowired
    UploadUtil uploadUtil;

    @GetMapping("/user/home")
    public String home(){

        User user = userService.getById(getProfileId());

        List<Post> posts = postService.list(new QueryWrapper<Post>()
                .eq("user_id", getProfileId())
                //三十天内
                //.gt("created", DateUtil.offsetDay(new Date(), -30))
                .orderByDesc("created")
        );
        req.setAttribute("user", user);
        req.setAttribute("posts", posts);

        return "/user/home";
    }
    @GetMapping("/user/set")
    public String set(){
        User user = userService.getById(getProfileId());

        req.setAttribute("user", user);
        return "/user/set";
    }

    @ResponseBody
    @PostMapping("/user/set")
    public Result doSet(User user){
        if(StrUtil.isNotBlank(user.getAvatar())){
            User temp = userService.getById(getProfileId());
            temp.setAvatar(user.getAvatar());
            userService.updateById(temp);

            AccountProfile profile = getProfile();
            profile.setAvatar(user.getAvatar());

            SecurityUtils.getSubject().getSession().setAttribute("profile", profile);


            return Result.success().action("/user/set#avatar");

        }


        if(StrUtil.isBlank(user.getUsername())){
            return Result.fail("昵称不能为空");
        }
        int count = userService.count(new QueryWrapper<User>()
                .eq("username", getProfile().getUsername())
                .ne("id", getProfileId()));
        if(count > 0){
            return Result.fail("昵称已被占用");

        }
        User temp = userService.getById(getProfileId());
        temp.setUsername(user.getUsername());
        temp.setGender(user.getGender());
        temp.setSign(user.getSign());
        userService.updateById(temp);

        AccountProfile profile = getProfile();
        profile.setUsername(temp.getUsername());
        profile.setSign(temp.getSign());
        SecurityUtils.getSubject().getSession().setAttribute("profile", profile);


        return Result.success().action("/user/set#info");
    }
    @ResponseBody
    @PostMapping("/user/upload")
    public Result uploadAvatar(@RequestParam(value = "file") MultipartFile file) throws IOException {
        return uploadUtil.upload(UploadUtil.type_avatar, file);
    }

    @ResponseBody
    @PostMapping("/user/repass")
    public Result repass(String nowpass, String pass, String repass) {
        if(!pass.equals(repass)) {
            return Result.fail("两次密码不相同");
        }

        User user = userService.getById(getProfileId());

        String nowPassMd5 = SecureUtil.md5(nowpass);
        if(!nowPassMd5.equals(user.getPassword())) {
            return Result.fail("密码不正确");
        }

        user.setPassword(SecureUtil.md5(pass));
        userService.updateById(user);

        return Result.success().action("/user/set#pass");
    }

    @GetMapping("/user/index")
    public String index(){

        return "/user/index";
    }

    @ResponseBody
    @GetMapping("/user/public")
    public Result userP(){
        IPage page = postService.page(getPage(), new QueryWrapper<Post>()
                .eq("user_id", getProfileId())
                .orderByDesc("created"));
        return Result.success(page);
    }

    @ResponseBody
    @GetMapping("/user/collection")
    public Result collection(){
        IPage page = postService.page(getPage(), new QueryWrapper<Post>()
                .inSql("id", "SELECT post_id FROM user_collection WHERE user_id =" + getProfileId())
        );

        return Result.success(page);
    }




    @GetMapping("/user/mess")
    public String mess(){
        IPage page = messageService.paging(getPage(), new QueryWrapper<UserMessage>()
                .eq("to_user_id", getProfileId())
                .orderByAsc("created")
        );
        req.setAttribute("pageData", page);
        return "/user/mess";
    }
    @ResponseBody
    @PostMapping("/message/remove")
    public Result msgRemove(Long id, @RequestParam(defaultValue = "false") Boolean all){

        boolean remove = messageService.remove(new QueryWrapper<UserMessage>()
                .eq("to_user_id", getProfileId())
                .eq(!all, "id", id));

        return remove? Result.success(null) : Result.fail("删除失败");
    }
    @ResponseBody
    @PostMapping("/message/nums")
    public Map msgNums(){
        int count = messageService.count(new QueryWrapper<UserMessage>()
                .eq("to_user_id", getProfileId())
                .eq("status", "0")
        );
        return MapUtil.builder("status", 0).put("count", count).build();
    }
}






















