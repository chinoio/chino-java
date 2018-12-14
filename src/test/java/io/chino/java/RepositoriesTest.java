package io.chino.java;

import io.chino.api.common.ChinoApiException;
import io.chino.api.repository.Repository;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.DeleteAll;
import io.chino.java.testutils.TestConstants;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class RepositoriesTest extends ChinoBaseTest {

    private static ChinoAPI chino_admin;
    private static Repositories test;

    @BeforeClass
    public static void beforeClass() throws IOException, ChinoApiException {
        ChinoBaseTest.beforeClass();
        chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        test = ChinoBaseTest.init(chino_admin.repositories);

        // cleanup repos
        ChinoBaseTest.checkResourceIsEmpty(
                test.list().getRepositories().isEmpty(),
                test
        );
    }

    @Test
    public void test_CRUD() throws IOException, ChinoApiException {
        /* CREATE */
        String repoDesc = "Repository created for RepositoriesTest";
        Repository c = test.create(repoDesc);
        assertNotNull("Repository was not created", c);

        /* READ */
        Repository r = test.read(c.getRepositoryId());
        assertEquals("Read object is different from original", c, r);

        /* UPDATE */
        String updatedDesc = "Updated repository";
        Repository u = test.update(c.getRepositoryId(), updatedDesc);
        assertNotEquals("Object was not updated", c, u);
        assertEquals("Update failed", updatedDesc, u.getDescription());

        /* DELETE */
        test.delete(u.getRepositoryId(), true);

        try {
            test.read(c.getRepositoryId());
            fail("Object was not deleted.");
        } catch (ChinoApiException e) {
            System.out.println("Success");
        }
    }

    @Test
    public void test_list() throws IOException, ChinoApiException {
        Repository[] repos = new Repository[5];
        synchronized (this) {
            for (int i=0; i<5; i++) {
                repos[i] = test.create("test_list_repo" + i);
                try {
                    wait(3000);
                } catch (InterruptedException ignored) {}
            }
        }

        /* LIST (no args) */
        List<Repository> list = test.list().getRepositories();
        int index = 0;
        for (Repository repo : repos) {
            assertTrue(
                    "repo" + ++index + " wasn't in the list.",
                    list.contains(repo)
            );
        }

        list.clear();
        /* LIST (2 args) */
        /* LIST (2 args) */
        int offset = 0;
        int limit = 2;
        assertEquals( "Wrong list size (1)",
                limit,
                test.list(offset, limit).getRepositories().size()
        );

        offset = repos.length - 1;
        limit = repos.length;
        assertEquals( "Wrong list size (2)",
                1,
                test.list(offset, limit).getRepositories().size()
        );

        offset = 2;
        limit = repos.length;
        assertEquals( "Wrong list size (3)",
                limit - offset,
                test.list(offset, limit).getRepositories().size()
        );
    }

    @AfterClass
    public static void afterClass() throws IOException, ChinoApiException {
        new DeleteAll().deleteAll(test);
    }
}