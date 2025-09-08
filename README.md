# monday-mirage

## Build local db (postgres) via docker
```shell
docker run --name yatta_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -p 5432:5432  -e POSTGRES_DB=yatta_db -d postgres
```