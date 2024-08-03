# JMockaroo
A Mockaaro Library for Java.


# In Class Usage
Declare the desired class with @MockableObject annotation

and use @MockElement() to specify your desired parameters.
```
@MockableObject
public class MockedClass {

    @MockElement(dataType = DataTypes.FirstName, blankPercentage = 0)
    public String Name;

    @MockElement(dataType = DataTypes.CarModel, blankPercentage = 0)
    public String CarModel;

    @MockElement(dataType = DataTypes.CarModelYear, blankPercentage = 20)
    public String CarModelYear;

    @MockElement(dataType = DataTypes.EmailAddress)
    public String Email;

    // constructor, getters and setters omitted

```



## Usage
````
var client = new MockarooClient("yourApiKey");

// Single object
var singleObject = client.getSingleMock(MockedClass.clazz);

// 50 Objects of desired type
var listObject = client.getMocks(MockedClass.clazz, 50);



````

