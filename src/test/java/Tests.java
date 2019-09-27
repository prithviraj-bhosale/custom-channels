import com.fasterxml.jackson.databind.ObjectMapper;
import com.morfeus.ntuc.model.preference.Preference;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Tests {
  @Test
  public void test() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    InputStream inputStream = new ClassPathResource("/Users/savinaysai/Desktop/Repo/ntuc/NtucSpringBootApplication/src/main/resources/mostPrefred.json").getInputStream();
    Preference preference = objectMapper.readValue(inputStream, Preference.class);
    inputStream.close();
    System.out.println(preference);
  }
}
