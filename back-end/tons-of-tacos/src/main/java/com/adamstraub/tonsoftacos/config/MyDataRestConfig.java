package com.adamstraub.tonsoftacos.config;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
//    utilize for ease of initial development. to be removed before production.
@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {


    private final EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager){
        entityManager = theEntityManager;

    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
//      allows calling menu items by their index id for ease of development
//      will eventually be negated with menu items called by menu item name as their uid
//        all other entities utilize a uid
        exposeIds(config);
// turn off default action of exposing available paths through the initial endpoint
        config.disableDefaultExposure();

    }

    private void exposeIds(RepositoryRestConfiguration config) {
        // get a list off all entity classes from entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        List<Class> entityClasses = new ArrayList<>();
        for (EntityType tempEntityType : entities) {
            entityClasses.add(tempEntityType.getJavaType());
        }
        //expose the ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
