# CardFlow

CardFlow is a project management tool similar to Trello that has been built using Java with Spring and Angular. The application provides an intuitive interface for users to create and manage tasks, lists and boards. It uses a MongoDB database to store the data.

## Introduction

CardFlow is a modern web-based project management application that allows users to organize and prioritize their tasks, lists, and boards. The application is designed to provide a simple and user-friendly experience that can be easily customized to meet the specific needs of your organization.

The application has been built using a combination of Java with Spring and Angular. This allows for easy integration with existing systems, and provides a solid foundation for building complex applications. In addition, the application uses a MongoDB database to store the data. This provides scalability, performance, and flexibility, making it an ideal choice for large-scale projects.

## Getting Started

### Prerequisites
Before you can run this application, you need to have the following installed on your system:

- [JAVA](https://jdk.java.net/11/) version 11
- [Spring](https://spring.io) version 2.0.0.RELEASE
- [Node JS](https://nodejs.org/en) version 9.11.3
- [Angular CLI](https://github.com/angular/angular-cli) version 15.2.3
- [MongoDB](https://www.mongodb.com) version 3.2.19
- [Maven](https://maven.apache.org)

### Installing
1. Clone the repositories to your local machine: Backend and Frontend.
```sh
 git clone https://github.com/timoterik/card.flow.web.git
 git clone https://github.com/timoterik/card.flow.srv.git
```
2. Open the terminal/command prompt and navigate to the project directory
   Run the following command to start the backend server:
```sh
mvn spring-boot:run
```
3. Open another terminal/command prompt and navigate to the frontend directory
   Run the following command to install the dependencies:
```sh
npm install
```
4. Run the following command to start the frontend server:
```sh
ng serve
```

5. Once the backend and frontend servers are running, you can access the CardFlow application in your browser by navigating to:
```sh
http://localhost:4200
```
The application will automatically reload if youchange any of the source files.

From here, you can create new boards, add lists, and create tasks within those lists. You can also drag and drop tasks to reorder them, as well as move tasks between lists.


### Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

### Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

### Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a
package that implements end-to-end testing capabilities.

### Further help
To get more help on the Angular CLI use `ng help` or go check out
the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.

## License
This project is licensed under the MIT License - see the LICENSE.md file for details.


