FROM tomcat

#RUN mkdir /usr/local/tomcat/webapps/studentapp

COPY ./target/heritage-api.war /usr/local/tomcat/webapps/heritage-api.war