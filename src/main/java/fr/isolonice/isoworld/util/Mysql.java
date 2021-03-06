/*
 * This file is part of IsoWorlds, licensed under the MIT License (MIT).
 *
 * Copyright (c) Edwin Petremann <https://github.com/Isolonice/>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.isolonice.isoworld.util;

import java.sql.*;

public class Mysql {
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private boolean autoReconnect;
    private Connection connection;

    public Mysql(String host, int port, String database, String username, String password, boolean autoReconnect) {
        setHost(host);
        setPort(port);
        setDatabase(database);
        setUsername(username);
        setPassword(password);
        setAutoReconnect(autoReconnect);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Mysql)) {
            return false;
        }
        Mysql other = (Mysql) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$host = getHost();
        Object other$host = other.getHost();
        if (this$host == null ? other$host != null : !this$host.equals(other$host)) {
            return false;
        }
        if (getPort() != other.getPort()) {
            return false;
        }
        Object this$database = getDatabase();
        Object other$database = other.getDatabase();
        if (this$database == null ? other$database != null : !this$database.equals(other$database)) {
            return false;
        }
        Object this$username = getUsername();
        Object other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) {
            return false;
        }
        Object this$password = getPassword();
        Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        if (isAutoReconnect() != other.isAutoReconnect()) {
            return false;
        }
        Object this$connection = getConnection();
        Object other$connection = other.getConnection();
        return this$connection == null ? other$connection == null : this$connection.equals(other$connection);
    }

    protected boolean canEqual(Object other) {
        return other instanceof Mysql;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $host = getHost();
        result = result * 59 + ($host == null ? 43 : $host.hashCode());
        result = result * 59 + getPort();
        Object $database = getDatabase();
        result = result * 59 + ($database == null ? 43 : $database.hashCode());
        Object $username = getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        Object $password = getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        result = result * 59 + (isAutoReconnect() ? 79 : 97);
        Object $connection = getConnection();
        result = result * 59 + ($connection == null ? 43 : $connection.hashCode());
        return result;
    }

    public String toString() {
        return "Mysql(host=" + getHost() + ", port=" + getPort() + ", database=" + getDatabase() + ", username=" + getUsername() + ", password=" + getPassword() + ", autoReconnect=" + isAutoReconnect() + ", connection=" + getConnection() + ")";
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAutoReconnect() {
        return this.autoReconnect;
    }

    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void connect() throws ClassNotFoundException, SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase() + "?useUnicode=true&characterEncoding=UTF-8&autoReconnect=" + Boolean.toString(isAutoReconnect()), getUsername(), getPassword());
    }

    public Statement query(String query)
            throws SQLException {
        return getConnection().createStatement();
    }

    public PreparedStatement prepare(String query)
            throws SQLException {
        return getConnection().prepareStatement(query);
    }
}