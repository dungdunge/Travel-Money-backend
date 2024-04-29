package GPTspring.gptserviceV2.gpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)  // null 값이 아닌 필드만 JSON에 포함
public class ContentDto {
    private String type;
    private String text;

    @JsonProperty("image_url")  // JSON 필드명을 "image_url"로 지정
    private ImageURLDto imageUrl;  // 필드명을 카멜케이스로 변경

    // 텍스트 콘텐츠 전용 생성자
    public ContentDto(String type, String text) {
        this.type = type;
        this.text = text;
    }

    // 이미지 URL 콘텐츠 전용 생성자
    public ContentDto(String type, ImageURLDto imageUrl) {
        this.type = type;
        this.imageUrl = imageUrl;
    }
}
