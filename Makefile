# Nombre del contenedor / proyecto
APP_NAME=bloodpressure-api
DB_NAME=bloodpressure-db
VOLUME_NAME=db_data

# Puerto local de la API
PORT=8080

# ==========================
# Construcción y ejecución
# ==========================

# Build del JAR sin tests
build:
	./gradlew clean build -x test --no-daemon

# Levantar todo con docker compose
up:
	sudo docker compose up -d

# Apagar contenedores
down:
	sudo docker compose down

# Reconstruir imagen de la API y levantar
rebuild: build
	sudo docker compose up -d --build

# ==========================
# Limpieza completa
# ==========================

# Eliminar contenedores, imágenes y volúmenes de Docker
clean-all:
	sudo docker compose down -v --rmi all --remove-orphans

# Apagar, eliminar volúmenes y reconstruir todo desde cero
reset:
	sudo docker compose down -v
	sudo docker volume rm $(VOLUME_NAME) || true
	sudo docker compose build --no-cache
	sudo docker compose up -d

# ==========================
# Logs y estado
# ==========================

# Ver logs de la API
logs:
	sudo docker logs -f $(APP_NAME)

# Ver contenedores activos
ps:
	sudo docker ps
