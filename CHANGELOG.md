Version 1.3.1
- Fix an issue with different context classes not being found
----
Version 1.3.0
- Added `requires` to JSON parsing
- Better error reporting
- Clean up
----
Version 1.2.0
- Added way to convert command to json
    - This is semi-operational as there is no way to fully recover lambdas
- Made it so that invalid classes and method in the JSON does not cause a fail, but does print warnings
- Added complex command test
- Fix infinite loop in tests
- Consolidated Parsers into one class
- Some renaming
----
Version 1.1.0
- Clean up extra code
- Fix issue with method not properly calling
- Better errors
- Improve Tests
----
Version 1.0.0
- Create Brigadier commands from json files