package hypermart.hypermart.utility;

import hypermart.hypermart.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class CommonUtils {

    public static boolean isNull(Object obj) {

        //일반 객체 체크
        if(obj == null) {
            return true;
        }

        //문자열 체크
        if ((obj instanceof String) && (((String)obj).trim().length() == 0)) {
            return true;
        }

        //리스트 체크
        if (obj instanceof List) {
            return ((List<?>)obj).isEmpty();
        }

        return false;
    }

    public static boolean isEmptyMultipartFile(List<MultipartFile> files) {
        return files.get(0).getContentType() == null;
    }

    public static ResponseEntity<String> makeResponseEntityForRedirect(
            String inputUrl,
            HttpServletRequest request
    ) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        String url = "http://localhost:8080" + inputUrl;
        String token = JwtAuthenticationFilter.resolveToken(request);
        httpHeaders.setBearerAuth(token);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                String.class
        );
    }
}
