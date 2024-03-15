package org.xavier.star.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.annotation.Auth;
import org.xavier.star.common.CommonErrorCode;
import org.xavier.star.common.CommonException;
import org.xavier.star.common.Result;
import org.xavier.star.dto.SessionData;
import org.xavier.star.entity.User;
import org.xavier.star.service.UserService;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.xavier.star.util.SessionUtils;

@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionUtils sessionUtils;

    /**
     * 用户登录
     * @param code
     * @return
     */
    @PostMapping("/login/{code}")
    @ApiOperation(value = "登录", response = SessionData.class)
    @ApiImplicitParam(name = "code", value = "code", required = true, paramType = "path", dataType = "String")
    public Result<SessionData> login(@NotBlank @PathVariable("code") String code) {
        try {
            return  Result.success(userService.login(code));
        }catch (CommonException e){
            return  Result.result(e.getCommonErrorCode());
        }
    }

    /**
     * 退出登录
     * @return
     */
    @Auth
    @PostMapping("/logout")
    @ApiOperation(value = "登出",response = Long.class)
    public Result<Long> logout(){
        try {
            Long userId = sessionUtils.getUserId();
            sessionUtils.invalidate();
            return Result.success(userId);
        }catch (CommonException e){
            return  Result.result(e.getCommonErrorCode());
        }
    }

    /**
     * 获取当前会话的个人信息
     * @return
     */
    @Auth
    @GetMapping(value = "", produces = "application/json")
    @ApiOperation(value = "获取当前会话的个人信息")
    public Result<User> getUserSessionData(){
        try{
            return Result.success(userService.getUserById(sessionUtils.getUserId()));
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    /**
     * 根据id获取用户信息
     * @param userId
     * @return
     */
    @Auth
    @GetMapping(value = "/user", produces = "application/json")
    @ApiOperation(value = "根据id获取用户信息")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "Long")
    public Result<User> getUserInformation(@RequestParam("userId") Long userId){
        try {
            return Result.success(userService.getUserById(userId));
        } catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    /**
     * 检查登录状态
     * @return
     */
    @GetMapping("/check")
    @ApiOperation(value = "检查登录态")
    public Result<String> checkSession(){
        try {
            if(sessionUtils.getSessionData()==null) return Result.fail("登录失效");
            return Result.success("已登录");
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    /**
     * 上传用户头像
     * @param file
     * @return
     */
    @Auth
    @PostMapping(value = "/upload",produces = "application/json")
    @ApiOperation(value = "上传用户头像")
    public Result<String> uploadPortrait(MultipartFile file){
        try{
            return Result.success(userService.uploadPortrait(file, sessionUtils.getUserId()));
        }
        catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    /**
     * 根据id更新用户信息
     * @param user
     * @return
     */
    @Auth
    @PutMapping
    @ApiOperation(value = "根据用户id修改用户信息")
    @ApiImplicitParam(name = "user", value = "用户信息(id必须有)", required = true, paramType = "body", dataTypeClass = User.class)
    public Result<String> updateUser(@NotNull @RequestBody User user){
        try{
            userService.updateById(user);
            return Result.success("更新成功");
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @GetMapping("/identity")
    @ApiOperation("获取用户身份, 0大学生, 1小朋友")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, paramType = "query", dataType = "Long")
    public Result<Integer> getUserIdentity(Long id) {
        try{
            User user = userService.getById(id);
            if(user != null) {
                return Result.success(user.getIdentity());
            }
            throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @PutMapping("/share")
    @ApiOperation("减少今日剩余的转发次数")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "Long")
    public Result<Integer> addShareTimes(Long userId) {
        try{
            return Result.success(userService.addShareTimes(userId));
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @PutMapping("/subscribe")
    @ApiOperation("关注公众号, 固定返回1")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "Long")
    public Result<Integer> subscribe(Long userId) {
        try{
            return Result.success(userService.subscribe(userId));
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @PutMapping("/signIn")
    @ApiOperation("签到, 返回今日签到次数")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "Long")
    public Result<Integer> signIn(Long userId) {
        try{
            return Result.success(userService.signIn(userId));
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @GetMapping("/signIn")
    @ApiOperation("检查今日是否签到")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "Long")
    public Result<Integer> isSignIn(Long userId) {
        try {
            return Result.success(userService.isSignIn(userId));
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }
}




