package pro.foreway.dataflow;

import com.amazon.dax.client.dynamodbv2.ClientConfig;
import com.amazon.dax.client.dynamodbv2.ClusterDaxAsyncClient;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import pro.foreway.dataflow.entity.Device;
import pro.foreway.dataflow.entity.Message;
import pro.foreway.dataflow.util.Properties;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author yaruliy
 */
public class Lambda implements RequestHandler<Message, Map<String, String>> {
    private static Logger logger = LoggerFactory.getLogger(Lambda.class);
    private final DynamoDBMapper dynamoMapper;
    private final ObjectMapper jsonMapper;


    public Lambda() {
        MDC.put("request_id", "Initialization");
        logger.info("Object initialization...");
        ClientConfig daxConfig = new ClientConfig()
                .withCredentialsProvider(new AWSCredentialsProvider() {
                    public AWSCredentials getCredentials() {
                        return new BasicAWSCredentials(Properties.accessKey, Properties.secretKey);
                    }

                    public void refresh() {
                    }
                })
                .withRegion("us-west-2")
                .withEndpoints(Properties.daxUrl);

        dynamoMapper = new DynamoDBMapper(new ClusterDaxAsyncClient(daxConfig));

        this.jsonMapper = new ObjectMapper();
        this.jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MDC.remove("request_id");
    }


    @Override
    public Map<String, String> handleRequest(Message input, Context context) {
        MDC.put("request_id", context.getAwsRequestId());
        MDC.put("function_name", context.getFunctionName());
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));

        Map<String, String> response = new HashMap<>();
//        response.put("input.message", input.getRecords().get(0).getSNS().getMessage());
        response.put("input.message", input.getMessage());
        response.put("date", new Date().toString());
        response.put("logger.class", logger.getClass().getCanonicalName());

        int status = 0;
        logger.info("Getting http status...");
        try {
            URL url = new URL("http://www.google.com/");
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof HttpURLConnection)
                status = ((HttpURLConnection) urlConnection).getResponseCode();
        } catch (IOException e) {
            logger.error("Http error: " + e.getMessage());
        }
        response.put("url.status", String.valueOf(status));

        Device device = dynamoMapper.load(Device.class, "dev.1031");
        response.put("device", device.getDevID() + "; " + device.getOwner());
        printMap(response);
        return response;
    }

    private void printMap(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();
        builder.append("\r{");
        for (String key : map.keySet())
            builder.append("\r\t").append(key).append(": ").append(map.get(key));
        builder.append("\r}\r");
        logger.info(builder.toString());
    }
}