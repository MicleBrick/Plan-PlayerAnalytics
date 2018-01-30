/*
 * Licence is provided in the jar as license.yml also here:
 * https://github.com/Rsl1122/Plan-PlayerAnalytics/blob/master/Plan/src/main/resources/license.yml
 */
package com.djrapitops.plan.system;

import com.djrapitops.plan.PlanBungee;
import com.djrapitops.plan.api.exceptions.EnableException;
import com.djrapitops.plan.system.database.BukkitDBSystem;
import com.djrapitops.plan.system.settings.Settings;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import utilities.mocks.BungeeMockUtil;

/**
 * Test for BukkitSystem.
 *
 * @author Rsl1122
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class BungeeSystemTest {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();
    private static PlanBungee planMock;
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private BungeeSystem bungeeSystem;

    @Before
    public void setUp() throws Exception {
        BungeeMockUtil mockUtil = BungeeMockUtil.setUp()
                .withDataFolder(temporaryFolder.getRoot())
                .withLogging()
                .withPluginDescription()
                .withResourceFetchingFromJar()
                .withProxy();
        planMock = mockUtil.getPlanMock();
    }

    @After
    public void tearDown() {
        if (bungeeSystem != null) {
            bungeeSystem.disable();
        }
    }

    @Test
    public void testEnable() throws EnableException {
        bungeeSystem = new BungeeSystem(planMock);

        Settings.WEBSERVER_PORT.setTemporaryValue(9000);
        Settings.BUNGEE_IP.setTemporaryValue("8.8.8.8");
        Settings.DB_TYPE.setTemporaryValue("sqlite");
        bungeeSystem.setDatabaseSystem(new BukkitDBSystem());

        bungeeSystem.enable();
    }

    @Test
    public void testEnableDefaultIP() throws EnableException {
        thrown.expect(EnableException.class);
        thrown.expectMessage("IP setting still 0.0.0.0 - Configure AlternativeIP/IP that connects to the Proxy server.");

        bungeeSystem = new BungeeSystem(planMock);

        Settings.WEBSERVER_PORT.setTemporaryValue(9000);
        Settings.DB_TYPE.setTemporaryValue("sqlite");
        bungeeSystem.setDatabaseSystem(new BukkitDBSystem());

        bungeeSystem.enable();
    }

    @Test
    public void testEnableNoMySQL() throws EnableException {
        thrown.expect(EnableException.class);
        thrown.expectMessage("Database failed to initialize");

        bungeeSystem = new BungeeSystem(planMock);
        bungeeSystem.enable();
    }
}