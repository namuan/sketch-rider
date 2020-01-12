package dev.deskriders.sketchrider.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import dev.deskriders.sketchrider.config.converter.LocalDateTimeDynamoDbConverter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@DynamoDBTable(tableName = "sketch-rider")
public class UserDiagramEntity {
    @DynamoDBHashKey(attributeName = "Id")
    private String id;

    @DynamoDBRangeKey(attributeName = "Metadata")
    private String metadata;

    @DynamoDBAttribute(attributeName = "DocumentCode")
    private String diagramCode;

    @DynamoDBAttribute(attributeName = "CreatedDateTime")
    @DynamoDBTypeConverted(converter = LocalDateTimeDynamoDbConverter.class)
    private LocalDateTime createdDateTime;

    @DynamoDBAttribute(attributeName = "OwnerName")
    private String ownerName;

    public void setOwnerId(String id) {
        this.id = "Owner-" + id;
    }

    public void setDocumentId(String docId) {
        this.metadata = "Doc-" + docId;
    }
}
