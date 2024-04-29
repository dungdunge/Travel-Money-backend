package GPTspring.gptserviceV2.gpt.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MessageDto {
    private String role;
    private List<ContentDto> content;

    public MessageDto(String role, List<ContentDto> content) {
        this.role = role;
        this.content = content;
    }
}
