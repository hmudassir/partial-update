# Partial update

`PATCH /api/profiles/1` changes only the fields present in the JSON object.

- A missing field stays as it is. `{"title":"Updated"}` does not clear `summary`.
- `summary: null` clears the summary. Null is a value, and it is different from leaving the field out.
- `title` is required when it is present. It cannot be null or blank.
- An unknown field, or an empty object, is `400`.

The stored row starts as title `Hello` and summary `Keep`.

## Run

Java 21.

```sh
./mvnw spring-boot:run
```

```sh
curl -sS -X PATCH -H 'Content-Type: application/json' \
  -d '{"title":"Updated"}' \
  http://127.0.0.1:8080/api/profiles/1
```

## Tests

```sh
./mvnw test
```

## What this is

A portfolio proof of concept. The architecture is in [docs/architecture.md](docs/architecture.md).
