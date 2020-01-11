package dev.deskriders.sketchrider.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import dev.deskriders.sketchrider.config.converter.LocalDateTimeDynamoDbConverter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@DynamoDBTable(tableName = "sketch-rider")
public class UserDiagramEntity {
    @DynamoDBHashKey(attributeName = "DocId")
    private String docId;

    @DynamoDBAttribute(attributeName = "OwnerId")
    private String ownerId;

    @DynamoDBAttribute(attributeName = "DocumentCode")
    private String diagramCode;

    @DynamoDBAttribute(attributeName = "CreatedDateTime")
    @DynamoDBTypeConverted(converter = LocalDateTimeDynamoDbConverter.class)
    private LocalDateTime createdDateTime;

    public boolean notOwnedBy(String ownerId) {
        return !ownedBy(ownerId);
    }

    public boolean ownedBy(String ownerId) {
        return this.ownerId.equals(ownerId);
    }
}
