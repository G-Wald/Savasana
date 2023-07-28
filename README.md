# Yoga starter

This project is in two parts.

The front end is on Angular and the back end Spring boot

## 1. Getting started

Make sure you have java on your computer

### 1.1. Get source code of Api

```
$ git clone https://github.com/G-Wald/Savasana.git
```

### 1.2. Get the database

Download xampp with this url
```
https://www.apachefriends.org/fr/index.html
```
Make sure to download Apache and SQL.

Launch Xampp, Apache and MySQl.

On the control panel click on Admin MySQL.

It should redirect you to this address.

http://localhost/phpmyadmin/

Click on new Database use utf8mb4_general_ci type

Once created use the SQL file to increment the database.

You can find it in the folder ressources


### 1.3. Lancer l'application

Before launch please enter your database information on application.properties

```
spring.datasource.url= "your db url"
spring.datasource.username="your db username"
spring.datasource.password="your db password"
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

Use an IDE to open the project and build it and run it

If you don't have an IDE, you can dowload Intellij.

Open the project with Intellij.

Go to SpringBootSecurityJwtApplication.

Build the application with the green hammer on the top of your window.

Run it with the button play. 

(If you can run and build check if you have a SDK install)


### 1.4. Front End

Go the front folder and open a command prompt.

use th following command :

```
npm install
```
to install the dependencies


```
ng serve
```
to run the front app


```
ng test
```
to execute test


### 1.5. End To End

Test end to end are done with cypress

Install Cypress dependencies by using :

```
npm install cypress --save-dev

```

To run the test use : 

```
npx cypress run

```