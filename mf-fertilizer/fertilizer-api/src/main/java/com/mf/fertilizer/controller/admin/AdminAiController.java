package com.mf.fertilizer.controller.admin;

import com.mf.fertilizer.config.AiClient;
import com.mf.fertilizer.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/ai")
@RequiredArgsConstructor
public class AdminAiController {

    private final AiClient aiClient;

    @PostMapping("/article/draft")
    public ResultVO<?> draftArticle(@RequestBody Map<String, String> body) {
        String topic = body.get("topic");
        String category = body.getOrDefault("category", "");
        if (topic == null || topic.isBlank()) return ResultVO.fail(400, "请输入文章主题");
        try {
            var result = aiClient.draftArticle(topic, category);
            return ResultVO.success(result);
        } catch (Exception e) {
            return ResultVO.fail(503, "AI 服务暂不可用: " + e.getMessage());
        }
    }

    @PostMapping("/encyclopedia/draft")
    public ResultVO<?> draftEncyclopedia(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        if (name == null || name.isBlank()) return ResultVO.fail(400, "请输入植物名称");
        try {
            var result = aiClient.draftEncyclopedia(name);
            return ResultVO.success(result);
        } catch (Exception e) {
            return ResultVO.fail(503, "AI 服务暂不可用: " + e.getMessage());
        }
    }

    @PostMapping("/knowledge/rebuild")
    public ResultVO<?> rebuild() {
        try {
            var result = aiClient.rebuildKnowledge();
            return ResultVO.success(result);
        } catch (Exception e) {
            return ResultVO.fail(503, "AI 服务暂不可用: " + e.getMessage());
        }
    }
}
