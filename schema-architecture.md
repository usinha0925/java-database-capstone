This Spring Boot application uses both MVC and REST controllers. Thymeleaf templates are used for the Admin and Doctor dashboards, while REST APIs serve all other modules. The application interacts with two databases—MySQL (for patient, doctor, appointment, and admin data) and MongoDB (for prescriptions). All controllers route requests through a common service layer, which in turn delegates to the appropriate repositories. MySQL uses JPA entities while MongoDB uses document models.
1. User accesses AdminDashboard or Appointment pages.
2. The action is routed to the appropriate Thymeleaf or REST controller.
3. The controller calls the service layer which acts as the heart of backend system.  This layer:
  - Applies business rules and validations
  - Coordinates workflows across multiple entities (e.g., checking doctor availability before scheduling an appointment)
  - Ensures a clean separation between controller logic and data access
4. The service layercommunicates with the Repository Layer to perform data access operations which include MySQL and MongoDB
5. Each repository interfaces directly with the underlying database engine with relational mapping used for MySQL and document based mapping to MongoDB
6. Once data is retrieved from the database, it is mapped into Java model classes that the application can work with.
7. Finally, the bound models are used in the response layer
    
