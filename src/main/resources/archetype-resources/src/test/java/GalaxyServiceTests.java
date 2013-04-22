package ${groupId};

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import ${groupId}.GalaxyService;
import ${groupId}.SpringConfiguration;
import ${groupId}.World;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfiguration.class })
@Transactional
public class GalaxyServiceTests {

    @Autowired
    private GalaxyService galaxyService;

    @Autowired
    private Neo4jTemplate template;

    @Rollback(false)
    @BeforeTransaction
    public void cleanUpGraph() {
        Neo4jHelper.cleanDb(template);
    }

    @Test
    public void shouldAllowDirectWorldCreation() {
        assertEquals(0, galaxyService.getNumberOfWorlds());
        World myWorld = galaxyService.createWorld("mine", 0);
        assertEquals(1, galaxyService.getNumberOfWorlds());

        Iterable<World> foundWorlds = galaxyService.getAllWorlds();
        World mine = foundWorlds.iterator().next();
        assertEquals(myWorld.getName(), mine.getName());
    }

    @Test
    public void shouldHaveCorrectNumberOfWorlds() {
        galaxyService.makeSomeWorlds();
        assertEquals(13, galaxyService.getNumberOfWorlds());
    }

    @Test
    public void shouldFindWorldsById() {
        galaxyService.makeSomeWorlds();

        for (World world : galaxyService.getAllWorlds()) {
            World foundWorld = galaxyService.findWorldById(world.getId());
            assertNotNull(foundWorld);
        }
    }

    @Test
    public void shouldFindWorldsByName() {
        galaxyService.makeSomeWorlds();

        for (World world : galaxyService.getAllWorlds()) {
            World foundWorld = galaxyService.findWorldByName(world.getName());
            assertNotNull(foundWorld);
        }
    }

    @Test
    public void shouldReachMarsFromEarth() {
        galaxyService.makeSomeWorlds();

        World earth = galaxyService.findWorldByName("Earth");
        World mars = galaxyService.findWorldByName("Mars");

        assertTrue(mars.canBeReachedFrom(earth));
        assertTrue(earth.canBeReachedFrom(mars));
    }

    @Test
    public void shouldFindAllWorlds() {
        Collection<World> madeWorlds = galaxyService.makeSomeWorlds();
        Iterable<World> foundWorlds = galaxyService.getAllWorlds();

        int countOfFoundWorlds = 0;
        for (World foundWorld : foundWorlds) {
            assertTrue(madeWorlds.contains(foundWorld));
            countOfFoundWorlds++;
        }

        assertEquals(madeWorlds.size(), countOfFoundWorlds);
    }

    @Test
    public void shouldFindWorldsWith1Moon() {
        galaxyService.makeSomeWorlds();

        for (World worldWithOneMoon : galaxyService.findAllByNumberOfMoons(1)) {
            assertThat(worldWithOneMoon.getName(), is(anyOf(containsString("Earth"), containsString("Midgard"))));
        }
    }

    @Test
    public void shouldNotFindKrypton() {
        galaxyService.makeSomeWorlds();
        World krypton = galaxyService.findWorldByName("Krypton");
        assertNull(krypton);
    }

}
