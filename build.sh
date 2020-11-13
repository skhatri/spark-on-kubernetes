tag=$1
docker build -t localhost:5000/spark-k8s-hello:$tag .
docker push localhost:5000/spark-k8s-hello:$tag
docker tag localhost:5000/spark-k8s-hello:$tag skhatri/spark-k8s-hello:$tag
docker push skhatri/spark-k8s-hello:$tag
