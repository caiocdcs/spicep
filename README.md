# SPICE-P Code Challenge: Crypto wallet management

**Code Challenge by Swiss Post**

This program that helps people keep track of their “crypto money” (also known as token), like
BTC or ETH, in a special wallet. This wallet stores information about different types of cryptocurrencies, how much
of each person owns, and how much it costs right now.

## How to run

Run the docker-compose file to start the database. It uses a postgres instance.

```shell
docker-compose up -d
```

Then, start the application in the IDE or using:

```shell
mvn spring-boot:run
```

## Endpoints

- Create wallet
```shell
curl --location 'localhost:8080/api/wallets' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userEmail": "test@email.com"
}'
```

- Get wallet
```shell
curl --location 'localhost:8080/api/wallets/{walletId}
```


