package GPTspring.gptserviceV2.gpt.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ImageAnalysisRequestDto {
    private String model;
    private List<MessageDto> messages;
    private Response_format response_format;
    public ImageAnalysisRequestDto(String model, List<MessageDto> messages, Response_format response_format) {
        this.model = model;
        this.messages = messages;
        this.response_format = response_format;
    }

    public ImageAnalysisRequestDto(String model, List<MessageDto> messages) {
        this.model = model;
        this.messages = messages;
    }


}
