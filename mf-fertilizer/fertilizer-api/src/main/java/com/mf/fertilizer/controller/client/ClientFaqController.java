package com.mf.fertilizer.controller.client;

import com.mf.fertilizer.entity.Faq;
import com.mf.fertilizer.service.FaqService;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/client/faq")
@RequiredArgsConstructor
public class ClientFaqController {
    private final FaqService service;

    @GetMapping
    public ResultVO<List<Faq>> list(@RequestParam(name = "category", required = false) String category) {
        var list = service.lambdaQuery().eq(Faq::getIsPublished, 1)
                .eq(category != null, Faq::getCategory, category)
                .orderByAsc(Faq::getSortOrder).list();
        return ResultVO.success(list);
    }
}
