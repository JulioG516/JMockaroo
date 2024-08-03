# JMockaroo
**JMockaroo** is a simple Java library designed to interact with the Mockaroo API, allowing you to fetch mock data and populate your Java classes using annotations.




- **Annotation-Based**: Use annotations to specify how your classes should be populated with mock data.

## Get Your API Key

To obtain your API key, follow these steps:

1. Log in to your Mockaroo account.
2.  Look for the **My API Key** card.
3. Generate your key or copy if you already have one.

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

