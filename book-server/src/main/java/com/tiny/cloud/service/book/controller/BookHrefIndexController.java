package com.tiny.cloud.service.book.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.math.MathUtil;
import com.tiny.cloud.service.book.mapper.BookInfoMapper;
import com.tiny.cloud.service.book.model.*;
import com.tiny.cloud.spider.common.uaa.config.LoginFormVO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.support.SecurityWebApplicationContextUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        List<ChapterListBO> chapterListBOS = mapper.qryChapterList(id);
        model.put("chapters",chapterListBOS);
        model.put("book",mapper.getBookInfo(id));
        Optional<ChapterListBO> first = chapterListBOS.stream().findFirst();
        if (first.isPresent()){
            Long id1 = first.get().getId();
            ChapterListBO chapterInfo = mapper.getChapterInfo(id1);
            model.put("content",getContent(chapterInfo.getName(),mapper.getContentInfo(id1)));
            ChapterListBO chapterListBO = chapterListBOS.get(1);
            if (chapterListBO!=null){
                model.put("next",chapterListBO.getId());
            }
        }
        modelAndView.setViewName("content");
        return modelAndView;
    }

    private String getContent(String name, String contentInfo) {
        String replace = contentInfo.replace("<br>", "</p><p>");
        String s = "<p>" + replace;
        return "<h1>"+name+"</h1>"+s;
    }

    @GetMapping("/content")
    @ResponseBody
    public ContentVO index(@RequestParam("id")String id){
        String contentInfo = mapper.getContentInfo(Long.parseLong(id));
        ChapterListBO chapterInfo = mapper.getChapterInfo(Long.parseLong(id));
        ContentVO preOrNext = mapper.getPreOrNext(Long.parseLong(id));
        String content = getContent(chapterInfo.getName(), contentInfo);
        preOrNext.setContent(content);
        return preOrNext;
    }

    @GetMapping("/index")
    public String index(){
       return "login";
    }

    @GetMapping("/book")
    public ModelAndView books(ModelAndView mv){
        LoginFormVO principal = (LoginFormVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUserId();
        List<BookListVO> bookListVO=mapper.getUserBooks(userId);
        Map<String, Object> model = mv.getModel();
        model.put("books",bookListVO);
        mv.setViewName("index");
        return mv;
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
