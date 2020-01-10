package dev.deskriders.sketchrider.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

@Data
@DynamoDBTable(tableName = "sketch-rider")
public class UserDiagramEntity {
    @DynamoDBHashKey(attributeName = "DocId")
    private String docId;

    @DynamoDBAttribute(attributeName = "UserId")
    private String userId;

    @DynamoDBAttribute(attributeName = "DocumentCode")
    private String diagramCode;
}
