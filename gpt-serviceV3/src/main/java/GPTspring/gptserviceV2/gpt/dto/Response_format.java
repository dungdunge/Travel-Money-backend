package GPTspring.gptserviceV2.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Response_format {
    @JsonProperty("type")
    private String type;

    public Response_format(String type) {
        this.type = type;
    }
}
