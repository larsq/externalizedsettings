package dev.larsq.plugin.externalsettings;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExternalRepositoryPluginTest {
    private ExternalRepositoryPlugin plugin;
    private Project project;
    private Path homedir;
    private Path parentDir;
    private Path projectDir;

    @BeforeAll
    void setupDirectories() throws IOException {
        homedir = Files.createTempDirectory("home");
        projectDir = Files.createTempDirectory("project");
        parentDir = Files.createTempDirectory("parent");
    }

    @BeforeEach
    void setup() {

        project = ProjectBuilder.builder()
                .withProjectDir(projectDir.toFile())
                .withParent(ProjectBuilder.builder()
                        .withGradleUserHomeDir(homedir.toFile())
                        .withProjectDir(parentDir.toFile()).build())
                .withGradleUserHomeDir(homedir.toFile())
                .build();

        plugin = new ExternalRepositoryPlugin();
    }

    @AfterEach
    void teardown() throws IOException {
        cleanDirectory(homedir);
        cleanDirectory(parentDir);
        cleanDirectory(projectDir);
    }

    @Test
    void shouldReturnEmptyIfSettingsIsNotFound() {
        assertEquals(Optional.empty(), plugin.settings(project));
    }

    @Test
    void shouldReturnSettingsPrimaryFromProjectDir() throws IOException {
        createSettings(projectDir);

        assertEquals(projectDir.resolve(".settings.json"),
                plugin.settings(project).orElse(null));
    }

    @Test
    void shouldReturnSettingsFromParentProjectIfNotFindInProject() throws IOException {
        createSettings(parentDir);

        assertEquals(parentDir.resolve(".settings.json"),
                plugin.settings(project).orElse(null));

    }

    @Test
    void shouldReturnSettingsInGraldeHomeAsFallback() throws IOException {
        createSettings(homedir);

        assertEquals(homedir.resolve(".settings.json"),
                plugin.settings(project).orElse(null));
    }

    private void createSettings(Path dir) throws IOException {
        InputStream in = Objects.requireNonNull(getClass().getResourceAsStream("/setting.json"));
        Files.copy(in, dir.resolve(".settings.json"));
    }

    private void cleanDirectory(Path dir) throws IOException {
        Files.deleteIfExists(dir.resolve(".settings.json"));
    }
}