package hello;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

  private static final Logger log = LoggerFactory.getLogger(Application.class);

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  public static void main(String[] args) throws Exception {
    // todo - versuchen mit logging zu versuchen herauszufinden wie sich die Werte ändern
    System.setProperty("faketime.offset.seconds", "7000");
    log.info("Started the application at {}", dateFormat.format(new Date()));


    

    // log.info("cron expression is 0 45 15 ? * *");
    log.info("cron expression is every 5 minutes");
    SpringApplication.run(Application.class);
  }
}
