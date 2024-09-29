/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.generatorV3.generator;

/**
 *
 * @author Aca
 */
public class DBConfig {
    private String dbUrl;
    private String user;
    private String password;

    public DBConfig() {}
    public DBConfig(String dbUrl, String user, String password) throws RuntimeException {
        setDbUrl(dbUrl);
        setUser(user);
        this.password = password;
    }

    public String getDbUrl() {
        return dbUrl;
    }
    public final void setDbUrl(String dbUrl) throws RuntimeException {
        if(dbUrl == null || dbUrl.isEmpty())
            throw new RuntimeException("Databes url cannot be empty");
        this.dbUrl = dbUrl;
    }
    public String getUser() {
        return user;
    }
    public final void setUser(String user) throws RuntimeException {
        if(user == null || user.isEmpty())
            throw new RuntimeException("Username cannot be empty");
        this.user = user;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "DBConfig{" + "dbUrl=" + dbUrl + ", user=" + user + ", password=" + password + '}';
    }
    
}
