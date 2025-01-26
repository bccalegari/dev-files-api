MIGRATIONS_DIR = ./src/main/resources/db/migration

TIMESTAMP = $(shell date +"%s")

.PHONY: help
help:
	@echo "Available Commands:"
	@echo "    make migration name=<migration_name> | Create a new migration file."
	@echo "    make run args=<args> | Run the project."
	@echo "    make stop args=<args> | Stop the project."
	@echo "Example:"
	@echo "    make migration name=add_users_table"
	@echo "    make run args=-d"
	@echo "    make stop"

.PHONY: migration
migration:
ifndef name
	$(error Error: Migration name is required. Use: make migration name=<migration_name>)
else
	@mkdir -p $(MIGRATIONS_DIR)
	@touch $(MIGRATIONS_DIR)/V$(TIMESTAMP)__$(name).sql
	@echo "Migration created: $(MIGRATIONS_DIR)/V$(TIMESTAMP)__$(name).sql"
endif

.PHONY: run-dev
run:
ifdef args
	@echo "Running with args: $(args)"
	docker compose -f docker-compose.dev.yml up $(args)
else
	docker compose -f docker-compose.dev.yml up
endif

.PHONY: stop
stop:
ifdef args
	@echo "Stopping with args: $(args)"
	docker compose -f docker-compose.dev.yml down $(args)
else
	docker compose -f docker-compose.dev.yml down
endif