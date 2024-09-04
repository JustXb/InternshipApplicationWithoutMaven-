package service;

public enum ServiceMessages {
    ENTER_NAME("Enter Name :"),
    ENTER_AGE("Enter age :"),
    ENTER_ADDRESS("Enter Address :"),
    ENTER_PASSPORT("Enter passport number :"),
    ENTER_HOTEL("Enter hotel :"),
    WRONG_NAME("Неверное имя пользователя"),
    WRONG_AGE("Неверно указан возраст"),
    WRONG_ADDRESS("Неверно указан адрес"),
    WRONG_PASSPORT("Номер паспорта не валиден"),
    WRONG_COUNT_NUMBER_PASSPORT("Номер паспорта должен содержать ровно 6 цифр."),
    SELECT_GUEST("Выберите гостя по его ID");



    private final String description;

    ServiceMessages(String description) {
        this.description = description;
    }
}
