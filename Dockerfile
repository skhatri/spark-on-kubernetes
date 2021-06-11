ARG username
FROM $username/spark:v3.1.1
RUN mkdir -p /opt/app/jars
COPY --chown=app:app build/libs/spark-k8s-hello-all.jar /opt/app/jars/


