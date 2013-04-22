package ${groupId};

import org.springframework.data.neo4j.repository.CypherDslRepository;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface WorldRepository extends GraphRepository<World>, CypherDslRepository<World> {}
