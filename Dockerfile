ARG username
FROM $username/spark:v3.0.1
RUN mkdir -p /tmp/jars
COPY build/libs/spark-k8s-hello-all.jar /tmp/jars/


