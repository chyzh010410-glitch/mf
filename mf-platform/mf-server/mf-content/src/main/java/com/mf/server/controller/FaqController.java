package com.mf.server.controller;
import com.mf.common.base.ResultVO;
import com.mf.server.entity.Faq;
import com.mf.server.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/client/faq") @RequiredArgsConstructor
public class FaqController {
    private final FaqService service;
    @GetMapping public ResultVO<List<Faq>> list(@RequestParam(required=false) String category) {
        var list=service.lambdaQuery().eq(Faq::getIsPublished,1).eq(category!=null,Faq::getCategory,category).orderByAsc(Faq::getSortOrder).list();
        return ResultVO.success(list);
    }
}
