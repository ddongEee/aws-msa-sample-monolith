# these will speed up builds, for docker-compose >= 1.25
export COMPOSE_DOCKER_CLI_BUILD=1
export DOCKER_BUILDKIT=1

project:=aws-msa-sample
service:=monolith

default: all

.PHONY: all
all: stop build start

.PHONY: start
start:
	docker-compose -p ${project} up -d

.PHONY: stop
stop:
	docker-compose -p ${project} down --remove-orphans

.PHONY: restart
restart: stop start

.PHONY: logs
logs:
	docker-compose -p ${project} logs -f ${service}

.PHONY: logs-db
logs-db:
	docker-compose -p ${project} logs -f ${service}-db

.PHONY: ps
ps:
	docker-compose -p ${project} ps

.PHONY: build
build:
	./gradlew clean build

.PHONY: clean
clean: stop build start

.PHONY: shell
shell:
	docker-compose -p ${project} exec ${service} sh

