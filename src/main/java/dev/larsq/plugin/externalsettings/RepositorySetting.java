package dev.larsq.plugin.externalsettings;

import java.util.Objects;

class RepositorySetting {
    private final String name;
    private final String url;
    private final String username;
    private final String password;

    public RepositorySetting(String name, String url, String username, String password) {
        this.name = name;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepositorySetting that = (RepositorySetting) o;
        return name.equals(that.name) &&
                url.equals(that.url) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, username, password);
    }

    @Override
    public String toString() {
        String username = this.username != null ? "<set>" : "<not set>";
        String password = this.password != null ? "<set>" : "<not set>";

        return "RepositorySetting{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", username=" + username +
                ", password=" + password +
                '}';
    }
}
