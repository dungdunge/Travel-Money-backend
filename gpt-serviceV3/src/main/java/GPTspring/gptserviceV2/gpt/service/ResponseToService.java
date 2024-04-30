package GPTspring.gptserviceV2.gpt.service;



import java.util.Map;

public interface ResponseToService {
    String  responseData(Map<String, Object> response);
    Map<String, Object> stringToJson(String newResult);


}
