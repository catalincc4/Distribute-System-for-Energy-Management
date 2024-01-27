container_name=$1
service_name=$2
if docker ps -a --format '{{.Names}}' | grep -q "^$container_name"; then
    commandvalue=$(docker inspect --format {{.State.Running}} $container_name)
    if [[ $commandvalue = "true" ]]; then
        docker exec -it $container_name bash -c 'bash run-initialization.sh && exit'
        echo $container_name is runnning
    else
        docker start $container_name
        docker exec -it $container_name bash -c 'bash run-initialization.sh && exit'
    fi
else
    docker-compose -f docker-compose.production.yml up -d $service_name
    docker exec -it $container_name bash -c 'bash run-initialization.sh && exit'
fi
