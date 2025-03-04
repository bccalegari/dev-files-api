FROM rabbitmq:4.0-rc-management-alpine
RUN apk add --no-cache gettext
COPY rabbit-definitions.json /etc/rabbitmq/definitions.template.json
COPY init.sh /usr/local/bin/setup_rabbitmq.sh
ENTRYPOINT [ "/bin/bash", "/usr/local/bin/setup_rabbitmq.sh" ]