package dev.deskriders.sketchrider.repository;

import dev.deskriders.sketchrider.config.DbConfig;
import dev.deskriders.sketchrider.model.UserDiagramEntity;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

@Slf4j
@Singleton
public class UserDiagramRepository {
    private DbConfig dbConfig;

    public UserDiagramRepository(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public String saveUserDiagram(String docId, String userId, String diagramCode) {
        UserDiagramEntity entity = new UserDiagramEntity();
        entity.setDocId(docId);
        entity.setUserId(userId);
        entity.setDiagramCode(diagramCode);
        dbConfig.dynamoDbMapper().save(entity);
        log.info("Created diagram {} for user {}", docId, userId);
        return docId;
    }
}
