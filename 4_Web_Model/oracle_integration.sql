-- =========================================================
-- CREATE APPLICATION SCHEMA
-- =========================================================

-- Remove the existing application user if it already exists

BEGIN
EXECUTE IMMEDIATE 'DROP USER GAMES CASCADE';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -1918 THEN -- ORA-01918: user does not exist
            RAISE;
END IF;
END;
/

-- Create the application schema

CREATE USER GAMES IDENTIFIED BY games_password
    DEFAULT TABLESPACE USERS
    TEMPORARY TABLESPACE TEMP
    QUOTA UNLIMITED ON USERS;

-- Grant privileges needed for application objects

GRANT CREATE SESSION TO GAMES;
GRANT CREATE TABLE TO GAMES;
GRANT CREATE VIEW TO GAMES;
GRANT CREATE PROCEDURE TO GAMES;
GRANT CREATE SEQUENCE TO GAMES;
GRANT CREATE TRIGGER TO GAMES;

GRANT CREATE ANY DIRECTORY TO GAMES;

GRANT READ, WRITE ON DIRECTORY DATA_DIR TO GAMES;

-- =========================================================
-- CONFIGURE DIRECTORY FOR EXTERNAL FILE ACCESS
-- =========================================================

CREATE OR REPLACE DIRECTORY DATA_DIR AS '/tmp';
GRANT READ, WRITE ON DIRECTORY DATA_DIR TO GAMES;

-- =========================================================
-- ENABLE HTTP ACCESS TO EXTERNAL API
-- =========================================================

BEGIN
    DBMS_NETWORK_ACL_ADMIN.DROP_ACL(acl => 'games_http_acl.xml');
EXCEPTION
    WHEN OTHERS THEN
        NULL;
END;
/

BEGIN
    DBMS_NETWORK_ACL_ADMIN.CREATE_ACL(
            acl         => 'games_http_acl.xml',
            description => 'Allow GAMES HTTP access',
            principal   => 'GAMES',
            is_grant    => TRUE,
            privilege   => 'connect'
    );
END;
/

BEGIN
    DBMS_NETWORK_ACL_ADMIN.ADD_PRIVILEGE(
            acl       => 'games_http_acl.xml',
            principal => 'GAMES',
            is_grant  => TRUE,
            privilege => 'resolve'
    );
END;
/

BEGIN
    DBMS_NETWORK_ACL_ADMIN.ASSIGN_ACL(
            acl        => 'games_http_acl.xml',
            host       => 'host.docker.internal',
            lower_port => 3000,
            upper_port => 3000
    );
END;
/

-- =========================================================
-- ENABLE SCHEMA IN ORDS
-- =========================================================

BEGIN
    ORDS_ADMIN.ENABLE_SCHEMA(
            p_enabled             => TRUE,
            p_schema              => 'GAMES',
            p_url_mapping_type    => 'BASE_PATH',
            p_url_mapping_pattern => 'games',
            p_auto_rest_auth      => FALSE
    );
COMMIT;
END;

GRANT INHERIT PRIVILEGES ON USER GAMES TO ORDS_METADATA;

GRANT INHERIT PRIVILEGES ON USER SYSTEM TO ORDS_METADATA;

GRANT INHERIT PRIVILEGES ON USER SYS TO ORDS_METADATA;

GRANT INHERIT PRIVILEGES ON USER PDBADMIN TO ORDS_METADATA;

GRANT INHERIT PRIVILEGES ON USER GAMES TO ORDS_METADATA;

-- =========================================================
-- EXPOSE VIEWS AS ORDS REST ENDPOINTS
-- =========================================================

BEGIN
    ORDS.ENABLE_OBJECT(
            p_enabled      => TRUE,
            p_schema       => 'GAMES',
            p_object       => 'THEMES_V',
            p_object_type  => 'VIEW',
            p_object_alias => 'themes'
    );

    ORDS.ENABLE_OBJECT(
            p_enabled      => TRUE,
            p_schema       => 'GAMES',
            p_object       => 'GAMES_V',
            p_object_type  => 'VIEW',
            p_object_alias => 'games_v'
    );

    ORDS.ENABLE_OBJECT(
            p_enabled      => TRUE,
            p_schema       => 'GAMES',
            p_object       => 'USER_RATINGS_V',
            p_object_type  => 'VIEW',
            p_object_alias => 'user_ratings'
    );

    ORDS.ENABLE_OBJECT(
            p_enabled      => TRUE,
            p_schema       => 'GAMES',
            p_object       => 'BOARDGAMES_FULL_V',
            p_object_type  => 'VIEW',
            p_object_alias => 'boardgames_full'
    );

    ORDS.ENABLE_OBJECT(
            p_enabled      => TRUE,
            p_schema       => 'GAMES',
            p_object       => 'GAME_RATING_STATS_V',
            p_object_type  => 'VIEW',
            p_object_alias => 'game_rating_stats'
    );

    ORDS.ENABLE_OBJECT(
            p_enabled      => TRUE,
            p_schema       => 'GAMES',
            p_object       => 'THEME_RATING_STATS_V',
            p_object_type  => 'VIEW',
            p_object_alias => 'theme_rating_stats'
    );

    ORDS.ENABLE_OBJECT(
            p_enabled      => TRUE,
            p_schema       => 'GAMES',
            p_object       => 'TOP_GAMES_V',
            p_object_type  => 'VIEW',
            p_object_alias => 'top_games'
    );

COMMIT;
END;


