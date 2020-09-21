package dev.larsq.plugin.externalsettings;

import com.google.gson.Gson;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.internal.impldep.com.google.common.io.Files;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class RepositoryConfigurationContainer {
    private final List<RepositorySetting> settings;

    public RepositoryConfigurationContainer(List<RepositorySetting> settings) {
        this.settings = settings;
    }

    public void configure(MavenArtifactRepository artifactRepository) {
        settings.stream()
                .filter(setting -> setting.getName().equals(artifactRepository.getName()))
                .forEach(setting -> {
                    artifactRepository.setUrl(URI.create(setting.getUrl()));

                    PasswordCredentials credentials = artifactRepository.getCredentials();
                    credentials.setUsername(setting.getUsername());
                    credentials.setPassword(setting.getPassword());
                });
    }


    @Override
    public String toString() {
        return "RepositoryConfigurationContainer{" +
                "settings=" + configurations() +
                '}';
    }

    private String configurations() {
        return settings.stream().map(RepositorySetting::getName).collect(Collectors.joining(",", "[", "]"));
    }

    public static RepositoryConfigurationContainer fromJson(InputStream stream) {
        return new Gson().fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), RepositoryConfigurationContainer.class);
    }

}
