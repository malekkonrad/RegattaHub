package pl.edu.agh.dp.config;

import pl.edu.agh.dp.api.Configuration;
import pl.edu.agh.dp.api.Orm;
import pl.edu.agh.dp.api.SessionFactory;
import pl.edu.agh.dp.entity.*;

/**
 * Konfiguracja ORM dla aplikacji demo.
 * W prawdziwej aplikacji Spring byłby to @Configuration bean.
 */
@org.springframework.context.annotation.Configuration
public class OrmConfig {

    private static SessionFactory sessionFactory;

    /**
     * Inicjalizuje i zwraca SessionFactory jako singleton.
     * Demonstruje konfigurację ORM z różnymi encjami.
     */
    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            // Pobierz konfigurację ze zmiennych środowiskowych lub użyj domyślnych
            String dbUrl = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : "jdbc:postgresql://localhost:5432/orm_demo";
            String dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "orm_user";
            String dbPassword = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "secret";
            
            Configuration config = Orm.configure()
                    .setProperty("db.url", dbUrl)
                    .setProperty("db.user", dbUser)
                    .setProperty("db.password", dbPassword)
                    .setProperty("db.driver", "org.postgresql.Driver")
                    .setProperty("orm.schema.auto", "drop-create")
                    // Rejestracja wszystkich encji
                    .register(
                            // Dziedziczenie JOINED - Account/BankAccount/SavingsAccount/InvestmentAccount
                            Account.class,
                            BankAccount.class,
                            SavingsAccount.class,
                            InvestmentAccount.class,
                            // Relacje - Employee/Department
                            Employee.class,
                            Department.class,
                            // Dziedziczenie SINGLE_TABLE - Document/Invoice/Report
                            Document.class,
                            Invoice.class,
                            Report.class,
                            Curriculum.class,
                            // Dziedziczenie TABLE_PER_CLASS - Notification
                            Notification.class,
                            EmailNotification.class,
                            SmsNotification.class,
                            PushNotification.class
                    );

            sessionFactory = config.buildSessionFactory();
        }
        return sessionFactory;
    }

    /**
     * Zamyka SessionFactory - do użycia przy zamykaniu aplikacji.
     */
    public static synchronized void shutdown() {
        if (sessionFactory != null) {
            sessionFactory = null;
        }
    }
}
