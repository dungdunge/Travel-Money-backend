package GPTspring.gptserviceV2.gpt.service.impl;

import GPTspring.gptserviceV2.gpt.config.ChatGPTConfig;
import GPTspring.gptserviceV2.gpt.dto.ContentDto;
import GPTspring.gptserviceV2.gpt.dto.ImageAnalysisRequestDto;
import GPTspring.gptserviceV2.gpt.dto.ImageURLDto;
import GPTspring.gptserviceV2.gpt.dto.MessageDto;
import GPTspring.gptserviceV2.gpt.service.ChatGPTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatGPTServiceImpl implements ChatGPTService {
    @Value("${openai.url.prompt}")
    private String promptUrl;

    private final ChatGPTConfig chatGPTConfig;
    String question1= "Extract text from the image above. The text you need to extract is the item and price, and the language above is Korean.";
    @Override
    public Map<String, Object> promptV2(ImageURLDto imageUrlDto) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        ContentDto imageContent = new ContentDto("image_url", imageUrlDto);
        ContentDto textContent = new ContentDto("text", question1);
        MessageDto message = new MessageDto("user", Arrays.asList(textContent, imageContent));


        ImageAnalysisRequestDto requestDto = new ImageAnalysisRequestDto("gpt-4-turbo", Collections.singletonList(message));
        // 요청 엔티티 생성
        return getStringObjectMap(resultMap, headers, requestDto);
    }

    private Map<String, Object> getStringObjectMap(Map<String, Object> resultMap, HttpHeaders headers, ImageAnalysisRequestDto requestDto) {

        ResponseEntity<String> response = chatGPTConfig.restTemplate()
                .exchange(promptUrl, HttpMethod.POST, new HttpEntity<>(requestDto, headers), String.class);
        Map<String, Object> contentMap = new HashMap<>();
        try {
            // GPT API 로부터 받은 JSON 형식의 응답 문자 파싱
            ObjectMapper om = new ObjectMapper();
            resultMap = om.readValue(response.getBody(), new TypeReference<>() {});

            // resultMap에서 'choices' 리스트를 추출하고 그 안의 'content' 값을 얻습니다.
            if (resultMap.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) resultMap.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    if (firstChoice.containsKey("message")) {
                        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                        if (message.containsKey("content")) {
                            // 'content' 키에 해당하는 값을 contentMap에 넣습니다.
                            contentMap.put("content", message.get("content"));
                        }
                    }
                }
            }
        } catch (JsonProcessingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }

        // 수정: 'content' 키에 해당하는 값만 포함하는 Map을 반환합니다.
        return contentMap;
    }


    @Override
    public Map<String, Object> prompt(ImageAnalysisRequestDto imageAnalysisRequestDt) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // 요청 엔티티 생성
        return getStringObjectMap(resultMap, headers, imageAnalysisRequestDt);
    }
}
