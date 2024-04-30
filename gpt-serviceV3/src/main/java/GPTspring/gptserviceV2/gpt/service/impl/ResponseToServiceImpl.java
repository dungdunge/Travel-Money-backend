package GPTspring.gptserviceV2.gpt.service.impl;

import GPTspring.gptserviceV2.gpt.config.ChatGPTConfig;
import GPTspring.gptserviceV2.gpt.dto.ContentDto;
import GPTspring.gptserviceV2.gpt.dto.ImageAnalysisRequestDto;
import GPTspring.gptserviceV2.gpt.dto.MessageDto;
import GPTspring.gptserviceV2.gpt.dto.Response_format;
import GPTspring.gptserviceV2.gpt.service.ResponseToService;
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
public class ResponseToServiceImpl implements ResponseToService {
    private final ChatGPTConfig chatGPTConfig;

    @Value("${openai.url.prompt}")
    private String promptUrl;


    String question1= "Could you please categorize the purchased items on this receipt into the categories ‘Food’, ‘Accommodation’, ‘Shopping’, ‘Transportation’, and ‘Other’ and format them in JSON format, including the name and price of each item?";
    @Override
    public String responseData(Map<String, Object> response) {
        String contentValue = (String) response.get("content");
        // 결과를 담을 Map
        Map<String, Object> resultMap = new HashMap<>();
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        ContentDto textContent = new ContentDto("text",contentValue + question1);
        MessageDto message = new MessageDto("user", Arrays.asList(textContent));

        // 응답 데이터 포멧 설정
        Response_format response_format = new Response_format("json_object");

        ImageAnalysisRequestDto requestDto = new ImageAnalysisRequestDto("gpt-3.5-turbo-1106", Collections.singletonList(message), response_format);
        // 요청 엔티티 생성
        return getStringObjectMap(resultMap, headers, requestDto);
    }

    @Override
    public Map<String, Object> stringToJson(String newResult) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();

        try {
            // JSON 문자열을 Map 객체로 변환
            jsonMap = objectMapper.readValue(newResult, new TypeReference<Map<String, Object>>() {});
            log.info("JSON to Map conversion successful.");
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to Map: {}", e.getMessage());
        }

        return jsonMap;
    }

    private String getStringObjectMap(Map<String, Object> resultMap, HttpHeaders headers, ImageAnalysisRequestDto requestDto) {
        HttpEntity<ImageAnalysisRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<String> response = chatGPTConfig.restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);
        String contentValue = null;

        try {
            // GPT API 로부터 받은 JSON 형식의 응답 문자 파싱
            ObjectMapper om = new ObjectMapper();
            Map<String, Object> responseMap = om.readValue(response.getBody(), new TypeReference<>() {});

            // responseMap에서 'choices' 리스트를 추출하고 그 안의 'content' 값을 얻습니다.
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
            if (!choices.isEmpty()) {
                Map<String, Object> firstChoice = choices.get(0);
                Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                contentValue = (String) message.get("content");
            }
        } catch (JsonProcessingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }

        return contentValue;
    }
}
