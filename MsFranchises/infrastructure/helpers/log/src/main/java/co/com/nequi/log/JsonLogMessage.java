package co.com.nequi.log;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class JsonLogMessage<T> {
    private String id;
    private String messageId;
    private String action;
    private String timestamp;
    private String application;
    private String service;
    private String component;
    private List<String> tags;
    private T data;
}