# Architecture

**Pattern:** a command object that records presence separately from the value. An omitted field and an explicit null are different instructions.

A DTO with `String summary` cannot tell those apart: both become null after JSON binding. This sample reads the JSON object and remembers which names were present.

## Boundaries

```text
PATCH /api/profiles/{id}
  JSON object
        |
        v
ProfileController
        |
        v
ProfileService
  reject unknown names
  reject an empty object
  for each present field, apply that value
        |
        v
ProfileRepository
  title is required
  summary is optional
```

The service loads the row, changes the present fields, and returns a response record. Fields that were absent are not assigned.

## Request path

1. `title` present: it must be a non-blank string. The stored title changes.
2. `summary` present and a string: the stored summary changes.
3. `summary` present and JSON null: the stored summary becomes null.
4. Any other name: `400`, and the row is unchanged because the exception is thrown before save.
5. No recognized field: `400`.

## Failure

| Body | Stored title | Stored summary |
| --- | --- | --- |
| `{"title":"Updated"}` | Updated | unchanged |
| `{"summary":null}` | unchanged | null |
| `{}` or `{"extra":1}` | unchanged | unchanged, `400` |
| Unknown id | | `404` |

## What this sample leaves out

Two fields. The same presence flags scale to a longer form. The rule to keep is: bind the object, record which keys arrived, then apply only those keys.
