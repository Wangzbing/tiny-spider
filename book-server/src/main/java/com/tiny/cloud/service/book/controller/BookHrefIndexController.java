package com.tiny.cloud.service.book.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.math.MathUtil;
import com.tiny.cloud.service.book.mapper.BookInfoMapper;
import com.tiny.cloud.service.book.model.BookInfoDO;
import com.tiny.cloud.service.book.model.VO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author wangzb
 * @date 2021/8/20
 * @description
 */
@Controller
public class BookHrefIndexController {
    @Resource
    BookInfoMapper mapper;

    @GetMapping("/{id}")
    public ModelAndView index(ModelAndView modelAndView, @PathVariable("id") Long id){
        Map<String, Object> model = modelAndView.getModel();
        model.put("chapters",mapper.qryChapterList(id));
        modelAndView.setViewName("content");
        return modelAndView;
    }

    @GetMapping("/content")
    @ResponseBody
    public String index(@RequestParam("id")String id){
        String contentInfo = mapper.getContentInfo(Long.parseLong(id));
        String replace = contentInfo.replace("<br>", "</p><p>");
        return "<p>"+replace;
    }

    @GetMapping("/index")
    public String index(){
       return "login";
    }

    @GetMapping
    public ModelAndView store(ModelAndView modelAndView){
        Map<String, Object> model = modelAndView.getModel();
        List<List<BookInfoDO>> split = ListUtil.split(mapper.qryBookList(), 6);
        ArrayList<String> strings = ListUtil.toList("随机推荐", "精品推荐", "全本推荐", "分类排行", "收藏推荐", "个人喜欢");
        AtomicInteger atomicInteger = new AtomicInteger();
        List<VO> collect = split.stream().map(s -> {
            VO vo = new VO();
            vo.setName(strings.get(atomicInteger.getAndIncrement()));
            s.forEach(x->{
                x.setBookImage("https:"+x.getBookImage());
                x.setBookIntro(x.getBookIntro().substring(0, Math.min(x.getBookIntro().length(), 50)));
            });
            vo.setMy(s);
            return vo;
        }).collect(Collectors.toList());
        model.put("allBooks",collect);
        modelAndView.setViewName("book_store");
        return modelAndView;
    }
}
