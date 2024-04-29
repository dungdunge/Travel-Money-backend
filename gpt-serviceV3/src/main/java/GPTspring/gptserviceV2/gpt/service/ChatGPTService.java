package GPTspring.gptserviceV2.gpt.service;


import GPTspring.gptserviceV2.gpt.dto.ImageAnalysisRequestDto;
import GPTspring.gptserviceV2.gpt.dto.ImageURLDto;

import java.util.Map;

public interface ChatGPTService {
     Map<String, Object> prompt(ImageAnalysisRequestDto imageAnalysisRequestDt);

     Map<String, Object> promptV2(ImageURLDto imageURLDto);
}
