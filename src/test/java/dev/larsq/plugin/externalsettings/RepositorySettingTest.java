package dev.larsq.plugin.externalsettings;

import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RepositorySettingTest {
    private RepositorySetting target;

    @BeforeEach
    void setup() {
        givenSetting("username", "password");
    }

    private void givenSetting(String username, String password) {
        target = new RepositorySetting("name", "http://example.com", username, password);
    }



    @Test
    void shouldReturnURL() {
        assertEquals("name", target.getName());
    }

    @Test
    void shouldReturnUrl() {
        assertEquals("http://example.com", target.getUrl());
    }

    @Test
    void shouldReturnUsername() {
        assertEquals("username", target.getUsername());
    }

    @Test
    void shouldReturnPassword() {
        assertEquals("password", target.getPassword());
    }

    @Test
    void shouldHaveTextRepresentationWhenUsernameAndPasswordAreSet() {
        givenSetting("username", "password");
        assertEquals("RepositorySetting{name='name', url='http://example.com', username=<set>, password=<set>}", target.toString());
    }

    @Test
    void shouldHaveTextRepresentationWhenOnlyUsernameIsSet() {
        givenSetting("username", null);
        assertEquals("RepositorySetting{name='name', url='http://example.com', username=<set>, password=<not set>}", target.toString());
    }

    @Test
    void shouldHaveTextRepresentationWhenUsernameAndPasswordIsNotSet() {
        givenSetting(null, null);
        assertEquals("RepositorySetting{name='name', url='http://example.com', username=<not set>, password=<not set>}", target.toString());
    }

    @Test
    void shouldDefineEqualsAndHashCode() {
        RepositorySetting instance = new RepositorySetting("name", "http://example.com", "user", "password");
        RepositorySetting clone = new RepositorySetting("name", "http://example.com", "user", "password");
        RepositorySetting other = new RepositorySetting("otherName", "http://example.com", "username", "password");

        assertAll(
                () -> assertNotEquals(null, instance, "equals null"),
                () -> assertNotEquals(other, instance, "same"),
                () -> assertEquals(clone, instance, "not same"),
                () -> assertEquals(instance, instance, "always same"),
                () -> assertEquals(clone.hashCode(), instance.hashCode(), "same value gives same hashcode")
        );
    }

}