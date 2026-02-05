//package pl.edu.agh.dp;
//
//import pl.edu.agh.dp.config.OrmConfig;
//import pl.edu.agh.dp.controller.*;
//
///**
// * Główna klasa demonstracyjna aplikacji.
// *
// * Pokazuje działanie CoreORM poprzez symulowane REST API:
// * - CRUD operations
// * - Transaction management
// * - Inheritance strategies (JOINED, SINGLE_TABLE)
// * - Relationships (One-to-Many, Many-to-One, Self-referencing)
// * - Lazy loading
// */
//public class DemoApplication {
//
//    public static void main(String[] args) {
//        System.out.println("╔══════════════════════════════════════════════════════════════╗");
//        System.out.println("║           CoreORM Demo Application                            ║");
//        System.out.println("║   Demonstracja funkcjonalności ORM w kontekście Spring       ║");
//        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
//
//        try {
//            // Inicjalizacja ORM
//            System.out.println("Initializing ORM...");
//            OrmConfig.getSessionFactory();
//            System.out.println("ORM initialized successfully!\n");
//
//            // Uruchomienie demo
//            runDemo();
//
//        } catch (Exception e) {
//            System.err.println("Error during demo: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            // Cleanup
//            System.out.println("\nShutting down ORM...");
//            OrmConfig.shutdown();
//            System.out.println("ORM shutdown complete.");
//        }
//    }
//
//    private static void runDemo() {
//        printSection("1. EMPLOYEE CRUD OPERATIONS");
//        demonstrateEmployeeCrud();
//
//        printSection("2. DEPARTMENT HIERARCHY (Self-referencing)");
//        demonstrateDepartmentHierarchy();
//
//        printSection("3. CLIENT - JOINED INHERITANCE");
//        demonstrateJoinedInheritance();
//
//        printSection("4. DOCUMENT - SINGLE_TABLE INHERITANCE (Polymorphism)");
//        demonstrateSingleTableInheritance();
//
//        printSection("5. COMPLEX SCENARIO - Employee-Department Relations");
//        demonstrateComplexRelations();
//    }
//
//    private static void demonstrateEmployeeCrud() {
//        EmployeeController controller = new EmployeeController();
//        controller.demonstrateCrudOperations();
//    }
//
//    private static void demonstrateDepartmentHierarchy() {
//        DepartmentController controller = new DepartmentController();
//        controller.demonstrateHierarchyOperations();
//    }
//
//    private static void demonstrateJoinedInheritance() {
//        ClientController controller = new ClientController();
//        controller.demonstrateInheritanceOperations();
//    }
//
//    private static void demonstrateSingleTableInheritance() {
//        DocumentController controller = new DocumentController();
//        controller.demonstratePolymorphicOperations();
//    }
//
//    private static void demonstrateComplexRelations() {
//        System.out.println("=== Complex Relations Demo ===\n");
//        System.out.println("This demo shows how ORM handles:");
//        System.out.println("- Many-to-One: Employee -> Department");
//        System.out.println("- One-to-Many: Department -> Employees");
//        System.out.println("- Self-reference: Employee -> Manager/Subordinates\n");
//
//        DepartmentController deptController = new DepartmentController();
//        EmployeeController empController = new EmployeeController();
//
//        // Tworzenie działu
//        System.out.println("1. Creating department...");
//        var deptDto = pl.edu.agh.dp.demo.dto.DepartmentDto.builder()
//                .name("Engineering")
//                .code("ENG")
//                .description("Engineering Department")
//                .build();
//        var deptResponse = deptController.createDepartment(deptDto);
//        System.out.println("   Created: " + deptResponse.getData());
//
//        if (deptResponse.isSuccess() && deptResponse.getData() != null) {
//            Long deptId = deptResponse.getData().getId();
//
//            // Tworzenie managera
//            System.out.println("\n2. Creating manager...");
//            var managerDto = pl.edu.agh.dp.demo.dto.EmployeeDto.builder()
//                    .firstName("Piotr")
//                    .lastName("Manager")
//                    .email("piotr.manager@company.com")
//                    .employeeCode("MGR001")
//                    .position("Engineering Manager")
//                    .departmentId(deptId)
//                    .build();
//            var managerResponse = empController.createEmployee(managerDto);
//            System.out.println("   Created: " + managerResponse.getData());
//
//            if (managerResponse.isSuccess() && managerResponse.getData() != null) {
//                Long managerId = managerResponse.getData().getId();
//
//                // Tworzenie pracowników
//                System.out.println("\n3. Creating employees under manager...");
//                var emp1Dto = pl.edu.agh.dp.demo.dto.EmployeeDto.builder()
//                        .firstName("Tomek")
//                        .lastName("Developer")
//                        .email("tomek.dev@company.com")
//                        .employeeCode("DEV001")
//                        .position("Senior Developer")
//                        .departmentId(deptId)
//                        .managerId(managerId)
//                        .build();
//                var emp1Response = empController.createEmployee(emp1Dto);
//                System.out.println("   Created: " + emp1Response.getData());
//
//                var emp2Dto = pl.edu.agh.dp.demo.dto.EmployeeDto.builder()
//                        .firstName("Kasia")
//                        .lastName("Developer")
//                        .email("kasia.dev@company.com")
//                        .employeeCode("DEV002")
//                        .position("Junior Developer")
//                        .departmentId(deptId)
//                        .managerId(managerId)
//                        .build();
//                var emp2Response = empController.createEmployee(emp2Dto);
//                System.out.println("   Created: " + emp2Response.getData());
//
//                // Pobieranie pracowników działu
//                System.out.println("\n4. Fetching employees in department (One-to-Many)...");
//                var deptEmployees = empController.getEmployeesByDepartment(deptId);
//                System.out.println("   " + deptEmployees.getMessage());
//                deptEmployees.getData().forEach(e ->
//                    System.out.println("   - " + e.getFirstName() + " " + e.getLastName()));
//
//                // Pobieranie podwładnych
//                System.out.println("\n5. Fetching subordinates (Self-reference)...");
//                var subordinates = empController.getSubordinates(managerId);
//                System.out.println("   " + subordinates.getMessage());
//                subordinates.getData().forEach(e ->
//                    System.out.println("   - " + e.getFirstName() + " " + e.getLastName()));
//
//                // Cleanup
//                System.out.println("\n6. Cleaning up...");
//                if (emp1Response.getData() != null) empController.deleteEmployee(emp1Response.getData().getId());
//                if (emp2Response.getData() != null) empController.deleteEmployee(emp2Response.getData().getId());
//                empController.deleteEmployee(managerId);
//            }
//            deptController.deleteDepartment(deptId);
//            System.out.println("   Cleanup completed");
//        }
//
//        System.out.println("\n=== Demo completed ===");
//    }
//
//    private static void printSection(String title) {
//        System.out.println("\n");
//        System.out.println("═".repeat(70));
//        System.out.println("  " + title);
//        System.out.println("═".repeat(70));
//        System.out.println();
//    }
//}
