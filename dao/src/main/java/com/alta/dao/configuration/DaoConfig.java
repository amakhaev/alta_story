package com.alta.dao.configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the DAO config file as POJO.
 */
@Getter
@Setter
public class DaoConfig {

    private DatabaseConnectionInfo dbCon;

    @Getter
    @Setter
    public static class DatabaseConnectionInfo {
        private String host;
        private int port;
    }

}
