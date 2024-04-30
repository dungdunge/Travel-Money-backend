package GPTspring.gptserviceV2.gpt.controller;

import GPTspring.gptserviceV2.gpt.dto.ImageAnalysisRequestDto;
import GPTspring.gptserviceV2.gpt.dto.ImageURLDto;
import GPTspring.gptserviceV2.gpt.service.ChatGPTService;
import GPTspring.gptserviceV2.gpt.service.ResponseToService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/chatGpt")
@RequiredArgsConstructor
public class ChatGPTController {
    private final ChatGPTService chatGPTService;
    private final ResponseToService responseToService;

    @PostMapping("/prompt")
    public ResponseEntity<Map<String, Object>> selectPrompt(@RequestBody ImageAnalysisRequestDto imageAnalysisRequestDto){
        Map<String, Object> result = chatGPTService.prompt(imageAnalysisRequestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/imagePrompt")
    public String imagePrompt(@RequestBody ImageURLDto imageURLDto){
        Map<String, Object> result = chatGPTService.promptV2(imageURLDto);
        String newResult = responseToService.responseData(result);
        responseToService.stringToJson(newResult);
        return newResult;
        // return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PostMapping("/imagePrompt1")
    public ResponseEntity<Map<String, Object>> imagePrompt1(@RequestBody ImageURLDto imageURLDto){
        Map<String, Object> result = chatGPTService.promptV2(imageURLDto);
        String newResult = responseToService.responseData(result);
        return new ResponseEntity<>(responseToService.stringToJson(newResult), HttpStatus.OK);
    }

    public String test(String result){
        return result;
    }

}
