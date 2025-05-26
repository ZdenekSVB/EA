package cz.mendelu.ea.utils;

import cz.mendelu.ea.domain.user.User;

import static org.hamcrest.MatcherAssert.assertThat;

import cz.mendelu.ea.domain.user.UserService;
import cz.mendelu.ea.utils.data.DataImporter;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

public class DataImporterUnitTest {

    @Test
    public void testParseUsers() throws IOException {
        // given
        Resource resource = mock(Resource.class);
        when(resource.getFile()).thenReturn(new File("src/test/resources/data/users.csv"));

        DataImporter dataImporter = new DataImporter(null, null);

        // when
        List<User> users = dataImporter.parseUsers(resource);

        // then
        assertThat(users, hasSize(7));
        assertThat(users.get(0).getName(), equalTo("Ann"));
        assertThat(users.get(0).getUsername(), equalTo("ann01"));
        assertThat(users.get(1).getName(), equalTo("John"));
        assertThat(users.get(1).getUsername(), equalTo("johndoe123"));
    }

    @Test
    public void testParseUsers_EmptyFile() throws IOException {
        // given
        Resource resource = mock(Resource.class);
        when(resource.getFile()).thenReturn(new File("src/test/resources/data/empty.csv"));

        DataImporter dataImporter = new DataImporter(null, null);

        // when
        List<User> users = dataImporter.parseUsers(resource);

        // then
        assertThat(users, hasSize(0));
    }

    @Test
    public void testParseUsers_NonExistingFile() throws IOException {
        // giventestParseUsers
        Resource resource = mock(Resource.class);
        when(resource.getFile()).thenReturn(new File("src/test/resources/data/non-existing.csv"));

        DataImporter dataImporter = new DataImporter(null, null);

        // when
        List<User> users = dataImporter.parseUsers(resource);

        // then
        assertThat(users, hasSize(0));
    }
}
