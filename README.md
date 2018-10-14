# JJParser

JJParser stands for JAVA JSON Parser which rectifies your JSON input according to a given schema.

  - Support JSON schema draft 07
  - Structure and data type correction
  - Written in Java
  - Only depended on Google gson

# Sample usage
> To demonstrate the functionality, let's choose a JSON input string
> which needs both data type and structural corrections. 
```java
    String schema = "{\n" +
                "  \"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
                "  \"type\": \"object\",\n" +
                "  \"properties\": {\n" +
                "      \"singleObjArray\": {\n" +
                "          \"type\": \"array\",\n" +
                "          \"items\": [{\"type\": \"number\"}]\n" +
                "      }\n" +
                "  }\n" +
                "}";
    String inputJson = "{\"singleObjArray\":\"1.618\"}";
    String result = JavaJsonParser.parseJson(inputJson, schema);
    System.out.println(result);
```
Which will give the following corrected output 
```java
{"singleObjArray":[1.618]}
```
### Development

Want to contribute? Great!

You can contribute by 
* Adding test cases.
* Reporting issues.
* Updating the project with latest schema changes.
* Add currently unsupported properties like "instaceOf"