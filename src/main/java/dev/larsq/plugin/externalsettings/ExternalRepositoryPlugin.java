package dev.larsq.plugin.externalsettings;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.publish.PublishingExtension;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public class ExternalRepositoryPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        target.afterEvaluate(this::applyInternal);
    }

    private void applyInternal(Project target) {
        Logger logger = target.getLogger();

        Optional<Path> settings = settings(target);
        if (settings.isPresent()) {
            try {
                RepositoryConfigurationContainer container =
                        RepositoryConfigurationContainer.fromJson(Files.newInputStream(settings.get()));

                logger.info("configuration: {}", container);

                target.getExtensions().getByType(PublishingExtension.class).getRepositories().configureEach(artifactRepository -> {
                    if (artifactRepository instanceof MavenArtifactRepository) {
                        logger.info("configure {}", artifactRepository.getName());
                        container.configure((MavenArtifactRepository) artifactRepository);
                    }
                });

            } catch (IOException e) {
                logger.error("Could not read setting", e);
                throw new UncheckedIOException(e);
            }
        } else {
            logger.info("No settings found: skipping configuration");
        }
    }

    Optional<Path> settings(Project project) {
        Project currentProject = Objects.requireNonNull(project);

        while (currentProject != null) {
            Path current = currentProject.getProjectDir().toPath().resolve(".settings.json");

            if (Files.isRegularFile(current)) {
                return Optional.of(current);
            }

            currentProject = currentProject.getParent();
        }

        return Optional.of(project.getGradle())
                .map(gradle -> gradle.getGradleUserHomeDir().toPath())
                .map(p -> p.resolve(".settings.json"))
                .filter(Files::isRegularFile);
    }


}
