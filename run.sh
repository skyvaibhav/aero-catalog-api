
LOCAL_IP=`ipconfig getifaddr en0`
docker run --rm -it \
-w="/code" \
-p 8080:8080 \
-e DB_URL=jdbc:mysql://$LOCAL_IP:3306/heritage \
-e DB_USER_NAME=root \
-e DB_PWD=mysql123c \
-v `pwd`:/code \
heritage-api:latest 
