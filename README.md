# Json To Brigadier 

This library converts JSON files into a corresponding `ArgumentBuilder` that represents a command structure. This is meant to reduce the challenge of lining up hard to see parenthesis and chained commands in your code.

## Usage

### Inclusion

Add `mavenCentral()` to the repositories section of your `build.gradle`

Add the dependency via `implementation "com.oroarmor:json-to-brigadier:1.0.0`

### Code

The API has two main methods to use. `JsonToBrigadier.parse(Path)` and `JsonToBrigadier.parse(String)`. The first method takes in a path to the file, and then calls the second with the contents of that file. They both return an `ArgumentBuilder` object representing the command structure specified by the JSON string or file.

The method given in `executes` must have the signature `public static int`.

Example:

```json
{
  "name": "test",
  "argument" : {
    "type": "brigadier:literal"
  },
  "children": [
    {
      "name": "value",
      "argument": {
        "type": "brigadier:integer",
        "min": 0,
        "max": 1
      },
      "executes": "com.example.TestSimpleCommand::runCommand"
    }
  ]
}
```
```java
ArgumentBuilder<T, S> builder = JsonToBrigadier.parse(json);
```

is the same as writing
```java
ArgumentBuilder<T, S> builder = literal("test")
    .then(argument("value", integer(0, 1))
        .executes(TestSimpleCommand::runCommand));
```

### Supported Types

This library only supports the default argument types in Brigadier. For a library that supports Minecraft's arguments, look at \<To be created>.

Type | Name | Parameters | Example
---- | ---- | ---------- | -------
Boolean | `"brigadier:boolean"` | None | ```"argument": { "type": "brigadier:boolean"} ```
Float | `"brigadier:float"` | `min`, `max`. `min` is required for `max` | ```"argument": { "type": "brigadier:float", "min": 1} ```
Double | `"brigadier:double"` | `min`, `max`. `min` is required for `max` | ```"argument": { "type": "brigadier:double", "min": 0, "max": 100} ```
Long | `"brigadier:long"` | `min`, `max`. `min` is required for `max` | ```"argument": { "type": "brigadier:long"} ```
Integer | `"brigadier:integer"` | `min`, `max`. `min` is required for `max` | ```"argument": { "type": "brigadier:integer"} ```
String | `"brigadier:string"` | `string_type`: `word`, `greedy`, `string`. Defaults to `string` | ```"argument": { "type": "brigadier:string"} ```
Literal | `"brigadier:literal"` | None | ```"argument": { "type": "brigadier:literal"} ```
