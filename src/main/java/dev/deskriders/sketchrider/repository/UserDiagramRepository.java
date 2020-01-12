package dev.deskriders.sketchrider.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import dev.deskriders.sketchrider.api.exception.BadRequestException;
import dev.deskriders.sketchrider.config.DbConfig;
import dev.deskriders.sketchrider.model.UserDiagramEntity;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class UserDiagramRepository {
    private DbConfig dbConfig;

    public UserDiagramRepository(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public String saveUserDiagram(String docId, String ownerId, String diagramCode) {
        UserDiagramEntity entity = new UserDiagramEntity();
        entity.setOwnerId(ownerId);
        entity.setDocumentId(docId);
        entity.setDiagramCode(diagramCode);
        entity.setCreatedDateTime(LocalDateTime.now());
        dbConfig.dynamoDbMapper().save(entity);
        log.info("Created diagram {} for user {}", docId, ownerId);
        return docId;
    }

    public void deleteUserDiagram(String ownerId, String docId) {
        UserDiagramEntity entity = new UserDiagramEntity();
        entity.setOwnerId(ownerId);
        entity.setDocumentId(docId);
        UserDiagramEntity existingUserDiagramEntity = dbConfig.dynamoDbMapper().load(entity);
        if (existingUserDiagramEntity == null) {
            throw new BadRequestException("Unable to find diagram " + docId);
        }

        dbConfig.dynamoDbMapper().delete(entity);
    }

    public List<UserDiagramEntity> selectUserDiagrams(String ownerId) {
        DynamoDBQueryExpression<UserDiagramEntity> queryExpression =
                new DynamoDBQueryExpression<>();
        UserDiagramEntity hkValue = new UserDiagramEntity();
        hkValue.setOwnerId(ownerId);
        queryExpression.withHashKeyValues(hkValue);
        PaginatedQueryList<UserDiagramEntity> queryList = dbConfig.dynamoDbMapper().query(UserDiagramEntity.class, queryExpression);
        return new ArrayList<>(queryList);
    }
}
