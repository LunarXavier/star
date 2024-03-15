package org.xavier.star.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.annotation.Admin;
import org.xavier.star.annotation.Auth;
import org.xavier.star.common.CommonException;
import org.xavier.star.common.Result;
import org.xavier.star.dto.MaterialWithImg;
import org.xavier.star.entity.Material;
import org.xavier.star.entity.MaterialImg;
import org.xavier.star.entity.RegistrationRecord;
import org.xavier.star.service.MaterialImgService;
import org.xavier.star.service.MaterialService;

import java.util.List;

@Slf4j
@Api(tags = "学习资料接口")
@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialImgService materialImgService;

    @Auth
    @GetMapping("/tag/{tag}")
    @ApiOperation("根据标签获取学习资料")
    @ApiImplicitParam(name = "tag", value = "学习资料标签", required = true, paramType = "path", dataType = "Integer")
    public Result<List<Material>> getByTag(@PathVariable Integer tag) {
        try {
            LambdaQueryWrapper<Material> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Material::getTag, tag);
            queryWrapper.orderByDesc(Material::getUpdateTime);
            queryWrapper.orderByDesc(Material::getDownloadTimes);
            List<Material> materials = materialService.list(queryWrapper);
            return Result.success(materials);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @GetMapping("{id}")
    @ApiOperation("根据id获取学习资料信息和对应图片列表(封装类)")
    @ApiImplicitParam(name = "id", value = "学习资料id", required = true, paramType = "path", dataType = "Long")
    public Result<MaterialWithImg> getById(@PathVariable Long id) {
        try {
            Material material = materialService.getById(id);
            LambdaQueryWrapper<MaterialImg> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MaterialImg::getMaterialId, id);
            queryWrapper.orderByAsc(MaterialImg::getCreateTime);
            List<MaterialImg> materialImgList = materialImgService.list(queryWrapper);
            MaterialWithImg materialWithImg = MaterialWithImg.builder()
                    .material(material)
                    .materialImgList(materialImgList)
                    .build();
            return Result.success(materialWithImg);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @PostMapping
    @ApiOperation("新增学习资料")
    @ApiImplicitParam(name = "material", value = "学习资料", required = true, paramType = "body", dataTypeClass = Material.class)
    public Result<String> insert(@RequestBody Material material) {
        try {
            materialService.save(material);
            return Result.success("新增学习资料成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @PutMapping
    @ApiOperation("修改学习资料")
    @ApiImplicitParam(name = "material", value = "学习资料", required = true, paramType = "body", dataTypeClass = Material.class)
    public Result<String> update(@RequestBody Material material) {
        try {
            boolean result = materialService.updateById(material);
            if (result) return Result.success("修改学习资料成功");
            else return Result.fail("修改学习资料失败");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @DeleteMapping
    @ApiOperation("根据id删除学习资料")
    @ApiImplicitParam(name = "id", value = "学习资料id", required = true, paramType = "query", dataType = "Long")
    public Result<String> delete(Long id) {
        try {
            if(materialService.deleteById(id)) return Result.success("删除学习资料成功");
            else return Result.fail("删除学习资料失败");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @PostMapping(value = "/upload", produces = "application/json")
    @ApiOperation(value = "上传学习资料封面")
    @ApiImplicitParam(name = "id", value = "学习资料id", required = true, paramType = "query", dataType = "Long")
    public Result<String> uploadPoster(MultipartFile file, Long id) {
        try{
            return Result.success(materialService.uploadCover(file, id));
        }
        catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @PostMapping(value = "/img", produces = "application/json")
    @ApiOperation(value = "上传学习资料缩略图")
    @ApiImplicitParam(name = "materialId", value = "学习资料id", required = true, paramType = "query", dataType = "Long")
    public Result<String> uploadImg(MultipartFile file, Long materialId) {
        try{
            return Result.success(materialImgService.uploadImg(file, materialId));
        }
        catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @DeleteMapping(value = "/img")
    @ApiOperation(value = "根据学习资料缩略图id删除学习资料缩略图")
    @ApiImplicitParam(name = "id", value = "学习资料缩略图id", required = true, paramType = "query", dataType = "Long")
    public Result<String> deleteImg(Long id) {
        try{
            if(materialImgService.removeById(id))
                return Result.success("删除学习资料缩略图成功");
            else return Result.fail("删除学习资料缩略图失败");
        }
        catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @GetMapping(value = "/img")
    @ApiOperation(value = "根据学习资料id获取对应的学习资料缩略图")
    @ApiImplicitParam(name = "materialId", value = "学习资料id", required = true, paramType = "query", dataType = "Long")
    public Result<List<MaterialImg>> getImg(Long materialId) {
        try{
            return Result.success(materialImgService.getImg(materialId));
        }
        catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }


    @Auth
    @PutMapping("/download")
    @ApiOperation("增加下载次数")
    @ApiImplicitParam(name = "id", value = "学习资料id", required = true, paramType = "query", dataType = "Long")
    public Result<Integer> addDownloadTimes(Long id) {
        try{
            return Result.success(materialService.addDownloadTimes(id));
        }
        catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }


    @Admin
    @GetMapping("/page")
    @ApiOperation(value = "分页查询所有学习资料，允许模糊查询学习资料名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize" ,value = "每页显示数量（不小于0）", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "pageNum", value = "页数（不小于0）", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "学习资料名称", required = false, paramType = "query", dataType = "String")
    })
    public Result<Page<Material>> page(@Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum, String name){
        try {
            //构造分页构造器
            Page<Material> pageInfo = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<Material> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(Material::getTitle, name);
            queryWrapper.orderByDesc(Material::getUpdateTime);
            //执行查询
            materialService.page(pageInfo, queryWrapper);
            return Result.success(pageInfo);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }
}
