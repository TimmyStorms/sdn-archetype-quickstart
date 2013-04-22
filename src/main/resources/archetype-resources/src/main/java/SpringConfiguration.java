package ${groupId};

import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

@Configuration
@EnableSpringConfigured
@ComponentScan("${groupId}")
@EnableNeo4jRepositories(basePackages = { "${groupId}" })
public class SpringConfiguration extends Neo4jConfiguration {

    @Bean(destroyMethod = "shutdown")
    public EmbeddedGraphDatabase graphDatabaseService() {
        return new EmbeddedGraphDatabase("/db/data");
    }
    
}
