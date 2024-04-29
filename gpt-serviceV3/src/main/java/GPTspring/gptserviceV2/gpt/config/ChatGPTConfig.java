package GPTspring.gptserviceV2.gpt.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ChatGPTConfig {

    @Value("${openai.secret-key}")
    private String secretKey;

    //restTemplate : HTTP 요청을 보내고 응답을 받는 데 사용
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    //  HttpHeaders 를 생성
    @Bean
    public HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        // Bearer 토큰을 설정
        headers.setBearerAuth(secretKey);
        // 내용 유형을 JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
