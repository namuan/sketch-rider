export PROJECTNAME=$(shell basename "$(PWD)")

.SILENT: ;               # no need for @

clean: ## Runs gradlew clean
	./gradlew clean

docker: ## Make docker image using Jib
	./gradlew jibDockerBuild

dynamo-local: ## Start local dynamo database
	docker-compose up -d dynamo

setup-local: dynamo-local ## Setup local environment (DynamoDb tables etc)
	aws dynamodb create-table --cli-input-json file://development/create-print-documents-table.json --endpoint-url http://localhost:8000

run-docker: docker ## Runs the service in a Docker container
	docker-compose up

run-local: dynamo-local ## Runs the service locally connecting with dynamodb in Docker
	./gradlew run

test-local: ## Test the service locally connecting with dynamodb in Docker
	./gradlew test

assemble: ## Gradle Assemble
	./gradlew assemble

.PHONY: help
.DEFAULT_GOAL := help

help: Makefile
	echo
	echo " Choose a command run in "$(PROJECTNAME)":"
	echo
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'
	echo