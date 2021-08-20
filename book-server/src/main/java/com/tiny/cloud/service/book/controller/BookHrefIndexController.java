package com.tiny.cloud.service.book.controller;

import com.tiny.cloud.service.book.mapper.BookInfoMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author wangzb
 * @date 2021/8/20
 * @description
 */
@Controller
public class BookHrefIndexController {
    @Resource
    BookInfoMapper mapper;

    @GetMapping
    public ModelAndView index(ModelAndView modelAndView){
        Map<String, Object> model = modelAndView.getModel();
        model.put("chapters",mapper.qryChapterList());
        modelAndView.setViewName("index");
        return modelAndView;
    }
    @GetMapping("/content")
    @ResponseBody
    public String index(@RequestParam("id")String id){
        String contentInfo = mapper.getContentInfo(Long.parseLong(id));
        String replace = contentInfo.replace("<br>", "</p><p>");
        return "<p>"+replace;
    }
}
