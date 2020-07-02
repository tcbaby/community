package com.tcbaby.community.web;

import com.tcbaby.community.service.DiscussPostService;
import com.tcbaby.community.vo.DiscussPostVo;
import com.tcbaby.community.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author tcbaby
 * @date 20/05/04 16:02
 */
@Controller
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;

    @GetMapping({"/", "/index"})
    public String getIndexPage(Model model, Page page) {
        // 设置总记录数、资源路径
        page.setTotal(discussPostService.queryDiscussPostRows(0));
        page.setPath("/index");
        // 分页查询 帖子及其发帖人
        List<DiscussPostVo> postVos = discussPostService.queryDiscussPosts(0, page.getPage(), page.getSize());
        model.addAttribute("postVos", postVos);
        return "/index";
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "/error/500";
    }
}
