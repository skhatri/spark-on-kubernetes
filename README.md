### Building Spark Runner
Build and Push Spark Runner
```
export SPARK_VERSION="v3.0.1"
$SPARK_HOME/bin/docker-image-tool.sh -r docker.io/$(whoami) -t ${SPARK_VERSION} -p $SPARK_HOME/kubernetes/dockerfiles/spark/bindings/python/Dockerfile build
docker push $(whoami)/spark:${SPARK_VERSION}
```


### Build Spark App
Build Spark Job and push to registry
```
export APP_VERSION="1.0.7"
./gradlew clean build
docker build --build-arg username=$(whoami) -t $(whoami)/spark-k8s-hello:${APP_VERSION} .
docker push $(whoami)/spark-k8s-hello:${APP_VERSION}
```

### Running Locally Against Kubernetes Proxy
Proxy Kubernetes Master API
```
kubectl proxy
```

Perform spark submit without using a driver image
```
$SPARK_HOME/bin/spark-submit \
    --master k8s://http://localhost:8001 \
    --deploy-mode cluster \
    --name hello \
    --class demo.Count \
    --conf spark.executor.instances=2 \
    --conf spark.kubernetes.container.image=$(whoami)/spark-k8s-hello:${APP_VERSION} \
    local:///tmp/jars/spark-k8s-hello.jar
```

### Running Locally
Run without Kubernetes
```
$SPARK_HOME/bin/spark-submit \
    --deploy-mode client \
    --name hello \
    --class demo.Count \
    --conf spark.executor.instances=1 \
    local://./build/libs/spark-k8s-hello.jar
```

### Running Against Kubernetes

Replace template variables
```
sed -es/__USERNAME__/$(whoami)/g -es/__APP_VERSION__/${APP_VERSION}/g -es/__SPARK_VERSION__/${SPARK_VERSION}/g deploy/launcher.template.yaml > deploy/launcher.yaml
 
```

``` 
kubectl apply -f deploy/sa.yaml
kubectl apply -f deploy/rbac.yaml
kubectl apply -f deploy/launcher.yaml
```