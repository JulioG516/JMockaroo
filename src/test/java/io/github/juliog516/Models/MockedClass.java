package io.github.juliog516.Models;

import io.github.juliog516.Helpers.DataTypes;
import io.github.juliog516.Helpers.MockElement;
import io.github.juliog516.Helpers.MockableObject;

import java.util.Objects;

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

    public MockedClass() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCarModel() {
        return CarModel;
    }

    public void setCarModel(String carModel) {
        CarModel = carModel;
    }

    public String getCarModelYear() {
        return CarModelYear;
    }

    public void setCarModelYear(String carModelYear) {
        CarModelYear = carModelYear;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockedClass that = (MockedClass) o;
        return Objects.equals(Name, that.Name) && Objects.equals(CarModel, that.CarModel) && Objects.equals(CarModelYear, that.CarModelYear) && Objects.equals(Email, that.Email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name, CarModel, CarModelYear, Email);
    }
}
