This Repo contain the implementaion of a Distributed System for Energy Management, the system it's been deploied on GoogleCloud, but probably it will not be available any more, because my free period experied
Technologies overview: Java Spring, Python, WebSockets, RabbitMQ, AngularJS, Docker, Google Cloud, MSSQL, CI/CD for GitLab 
- In every directory, you will see the CI/CD file for GitLab, because when I creted this system, I use GitLab
- To deploy this system, I created a VM on Google Cloud useing Docker to deploy the Microservices.
1. User-Management-Microservice:
   - Technologies: Java Spring + SQL Server
   - this microservices manage the users of the app
2. Device-Management-Microservice
      - Technologies: Java Spring + SQL Server 
    - this microservices manage the devices of the app
3. Device-Monitoring-Microservice
      - Technologies: Java Spring + SQL Server + WebSockets
      - this microservices recive data from a "Device", and it store the measurment of energy consumption, and send it thru WebSocket to the FrontedApp
4. Chat-Microservice
    - Technologies: Java Spring + SQL Server + WebSockets
    - this microservice it allows to the administrator to talk whit the users(device owners)
5. Device
   - this is a script in python which simulate a Device, which send data thru a RabbitMQ to the Monitoring Microservice
For security I use Spring Security + JWT => future implementation for https, I done it locally, but I don't use it because it was useless if I don't have a domain to generate a certificate for the app, I can see if it works properly
6. FrontendApp
   - AngularJS
   - it has some feature, if you are curios clone the repo, make it work on your machine,
     and see for your self(I think you can use the docker.compose file from the UserManagementMicroservice to deploy local your aplication useing Docker, and it will work. Don't worry docker is easy,
     you just need to catch the slick)
