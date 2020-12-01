tag=$1
docker build --build-arg username=$(whoami) -t localhost:5000/spark-k8s-hello:$tag .
docker push localhost:5000/spark-k8s-hello:$tag
docker tag localhost:5000/spark-k8s-hello:$tag $(whoami)/spark-k8s-hello:$tag
docker push $(whoami)/spark-k8s-hello:$tag
