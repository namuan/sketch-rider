package dev.deskriders.sketchrider.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import dev.deskriders.sketchrider.config.converter.LocalDateTimeDynamoDbConverter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@DynamoDBTable(tableName = "sketch-rider")
public class UserDocumentEntity {
    @DynamoDBHashKey(attributeName = "Id")
    private String id;

    @DynamoDBRangeKey(attributeName = "Metadata")
    private String metadata;

    @DynamoDBAttribute(attributeName = "DocumentCode")
    private String documentCode;

    @DynamoDBAttribute(attributeName = "CreatedDateTime")
    @DynamoDBTypeConverted(converter = LocalDateTimeDynamoDbConverter.class)
    private LocalDateTime createdDateTime;

    @DynamoDBAttribute(attributeName = "OwnerName")
    private String ownerName;

    @DynamoDBAttribute(attributeName = "DocumentTitle")
    private String documentTitle;

    @DynamoDBAttribute(attributeName = "DocumentType")
    private String documentType;

    @DynamoDBAttribute(attributeName = "DocumentStatus")
    private String documentStatus;

    public void setOwnerId(String id) {
        if (id.startsWith("Owner-")) {
            this.id = id;
        } else {
            this.id = "Owner-" + id;
        }
    }

    public String getOwnerId() {
        return this.id;
    }

    public void setDocumentId(String documentId) {
        if (documentId.startsWith("Document-")) {
            this.metadata = documentId;
        } else {
            this.metadata = "Document-" + documentId;
        }
    }

    public String getDocumentId() {
        return this.metadata;
    }
}
