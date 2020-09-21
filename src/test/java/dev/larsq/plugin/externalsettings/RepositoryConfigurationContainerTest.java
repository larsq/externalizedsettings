package dev.larsq.plugin.externalsettings;

import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.internal.impldep.com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepositoryConfigurationContainerTest {
    private final List<RepositorySetting> settings = Collections.unmodifiableList(Arrays.asList(
            setting("name", "http://example.com/1", "username", "password"),
            setting("other", "http://example.com/2", null, null)));

    private RepositoryConfigurationContainer container;

    @Mock
    private MavenArtifactRepository artifactRepository;

    @Mock
    private PasswordCredentials passwordCredentials;

    @BeforeEach
    void setup() {
        container = new RepositoryConfigurationContainer(settings);
    }

    @Test
    void shouldConfigureIfKnown() {
        giveMavenArtifactRepository("name", true);

        container.configure(artifactRepository);

        assertAll(
                () -> verify(artifactRepository).setUrl(URI.create("http://example.com/1")),
                () -> verify(passwordCredentials).setUsername("username"),
                () -> verify(passwordCredentials).setPassword("password")
        );
    }

    @Test
    void shouldIgnoreIfNotKnown() {
        giveMavenArtifactRepository("unknown", false);

        container.configure(artifactRepository);

        verify(artifactRepository, never()).setUrl(any());
        verifyNoInteractions(passwordCredentials);
    }

    @Test
    void shouldHaveTextRepresentation() {
        assertEquals("RepositoryConfigurationContainer{settings=[name,other]}", container.toString());
    }

    @Test
    void shouldCreateInstanceFromJson() {
        RepositoryConfigurationContainer actual = RepositoryConfigurationContainer.fromJson(Objects.requireNonNull(getClass().getResourceAsStream("/setting.json")));

        Gson gson = new Gson();

        assertEquals(gson.toJson(container), gson.toJson(actual));
    }


    private void giveMavenArtifactRepository(String name, boolean expectedActive) {
        when(artifactRepository.getName()).thenReturn(name);
        if (expectedActive) {
            when(artifactRepository.getCredentials()).thenReturn(passwordCredentials);
        }
    }

    private static RepositorySetting setting(String name, String url, String username, String password) {
        return new RepositorySetting(name, url, username, password);
    }


}