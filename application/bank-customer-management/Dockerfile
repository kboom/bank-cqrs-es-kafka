FROM openjdk:8-jre-alpine
WORKDIR /app
COPY target/bank-customer-management*.jar .
COPY entrypoint.sh .
RUN chmod +x *.sh
EXPOSE 8080
EXPOSE 8081
ENTRYPOINT ["./entrypoint.sh"]
CMD ["server"]