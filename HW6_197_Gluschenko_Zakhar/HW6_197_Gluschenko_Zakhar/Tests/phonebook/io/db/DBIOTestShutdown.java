package phonebook.io.db;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBIOTestShutdown {
    @Test
    void shutdown() throws SQLException {

        // БД подключена
        DBIO.start();

        // Отключаем ее - все ок
        assertDoesNotThrow(() -> DBIO.shutdown());

        // Повторно отключение - исключение
        assertThrows(SQLException.class, () -> DBIO.shutdown());

        // Повторно отключение - исключение
        assertThrows(SQLException.class, () -> DBIO.shutdown());
    }


//    @Test
//    void restartConnectDB() throws SQLException {
//        // БД подключена
//        DBIO.start();
//
//        // Отключаем ее - все ок
//        assertDoesNotThrow(() -> DBIO.shutdown());
//
//        // Повторно отключение - исключение
//        assertThrows(SQLException.class, () -> DBIO.shutdown());
//
//        // Подключаем заранее отключенную
//        assertDoesNotThrow(() -> DBIO.start());
//
//        // Отключаем ее - все ок
//        assertDoesNotThrow(() -> DBIO.shutdown());
//
//        // Повторно отключение - исключение
//        assertThrows(SQLException.class, () -> DBIO.shutdown());
//    }

//    @Test
//    void restartWorkingDB() throws SQLException {
//
//        // БД подключена
//        //DBIO.start();
//
//        //assertThrows(SQLException.class, () -> DBIO.start());
//
//        DBIO.shutdown();
//    }

}