plugins {
    id("java-gradle-plugin")
    id("maven-publish")
}

group = "dev.larsq.plugin"
version = "1.0.0"

dependencies {
    implementation(gradleApi())
    implementation("com.google.code.gson:gson:2.8.6")

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("org.mockito:mockito-junit-jupiter:3.5.11")
    testImplementation("org.mockito:mockito-inline:3.5.11");
    testImplementation("org.apache.commons:commons-lang3:3.11");

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")

}

repositories {
    jcenter()
}

gradlePlugin {
    plugins {
        create("ExternalRepositoryPlugin") {
            id = "dev.larsq.externalsettings.repository"
            implementationClass = "dev.larsq.plugin.externalsettings.ExternalRepositoryPlugin"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }

        repositories {
            mavenLocal()
        }
    }
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}