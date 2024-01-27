container_name=$1
service_name=$2

if (docker inspect --type container $container_name &> /dev/null) then
    commandvalue=$(docker inspect --format {{.State.Running}} $container_name)
    if [[ $commandvalue = "true" ]]; then 
        echo $container_name is runnning
        docker exec -d $container_name bash -c 'bash run-initialization.sh && exit'
    else
        docker start $container_name
        docker exec -d $container_name bash -c 'bash run-initialization.sh && exit'
    fi
else
    docker-compose -f docker-compose.production.yml up -d $service_name
    docker exec -d $container_name bash -c 'bash run-initialization.sh && exit'
fi
