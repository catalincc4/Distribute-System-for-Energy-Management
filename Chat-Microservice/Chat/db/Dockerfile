FROM mcr.microsoft.com/mssql/server

WORKDIR /usr/src/app
COPY . /usr/src/app

ENV SA_PASSWORD rootR12345678!
ENV ACCEPT_EULA Y
ENV MSSQL_PID Express
EXPOSE 1437
WORKDIR /usr/src/app
CMD /bin/bash /usr/src/app/entrypoint.sh