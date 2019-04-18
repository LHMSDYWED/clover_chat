package com.wechat.dynamic.controller;

import com.wechat.dynamic.pojo.Comment;
import com.wechat.dynamic.service.CommentService;
import com.wechat.dynamic.service.DynamicService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author jiangxin
 * @create 2019-01-20-16:18
 * 控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/comment")
@Transactional  //事务
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DynamicService dynamicService;

    /**
     * 查询所有
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",commentService.findAll());
    }

    /**
     * 更具id查询实体
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true, StatusCode.OK,"查询成功",commentService.findById(id));
    }

    /**
     * 根据id修改
     * @param id
     * @param comment
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable String id,@RequestBody Comment comment){
        comment.set_id(id);
        commentService.update(comment);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    /**
     * 跟进id删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id){
        dynamicService.minusComment( commentService.findById(id).getDynamicid());
        commentService.deleteById(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    /**
     * 增加评论
     * @param comment 前端只需传入评论信息
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.POST)
    public Result add(@RequestBody Comment comment,@PathVariable String id){
        //获取评论人的id
        String userid = (String) request.getAttribute("id");
        comment.setUserid(userid);
        comment.setDynamicid(id);
        commentService.add(comment);
        dynamicService.addComment(id);
        return new Result(true,StatusCode.OK,"评论成功");
    }
}
