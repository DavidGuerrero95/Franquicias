package co.com.nequi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetaDataResponse {
    private String messageId;
    private String requestDateTime;
    private String applicationId;
    private Boolean lastPageIndicator;
    private Integer totalRecords;
    private Boolean additionalRecords;
    private Integer pageSize;
    private Integer totalPageNumber;

}
