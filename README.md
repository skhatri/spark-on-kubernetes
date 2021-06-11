### Building Spark Runner
Set working registry
```
REGISTRY=""
CONTAINER_REGISTRY=""
REGISTRY="localhost:5000/"
CONTAINER_REGISTRY="192.168.64.1:5000/"
```

Build and Push Spark Runner
```
export SPARK_HOME=~/dev/tools/spark-3.1.1-bin-hadoop3.2
export SPARK_VERSION="v3.1.1"
#modified Dockerfile is provided as Dockerfile_spark
$SPARK_HOME/bin/docker-image-tool.sh -r docker.io/$(whoami) -t ${SPARK_VERSION} -p $SPARK_HOME/kubernetes/dockerfiles/spark/Dockerfile build
docker tag ${REGISTRY}$(whoami)/spark:${SPARK_VERSION}
docker push ${REGISTRY}$(whoami)/spark:${SPARK_VERSION}
```


### Build Spark App
Build Spark Job and push to registry
```
export APP_VERSION="1.0.9"
./gradlew clean build shadowJar
docker build --build-arg username=$(whoami) -t ${REGISTRY}$(whoami)/spark-k8s-hello:${APP_VERSION} .
docker push ${REGISTRY}$(whoami)/spark-k8s-hello:${APP_VERSION}
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
    --conf spark.kubernetes.container.image=${CONTAINER_REGISTRY}$(whoami)/spark-k8s-hello:${APP_VERSION} \
    local:///opt/app/jars/spark-k8s-hello-all.jar
```

### Running Locally
Run without Kubernetes
```
$SPARK_HOME/bin/spark-submit \
    --deploy-mode client \
    --name hello \
    --class demo.Count \
    --conf spark.executor.instances=1 \
    local://./build/libs/spark-k8s-hello-all.jar
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
