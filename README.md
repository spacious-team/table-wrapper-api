![java-version](https://img.shields.io/badge/Java-11-brightgreen?style=flat-square)
[![jitpack-last-release](https://jitpack.io/v/spacious-team/table-wrapper-api.svg?style=flat-square)](
https://jitpack.io/#spacious-team/table-wrapper-api)
[![Unit tests](https://img.shields.io/endpoint.svg?url=https%3A%2F%2Factions-badge.atrox.dev%2Fspacious-team%2Ftable-wrapper-api%2Fbadge%3Fref%3Ddevelop&style=flat-square&label=Test&logo=none)](
https://github.com/spacious-team/table-wrapper-api/actions/workflows/unit-tests.yml)
[![Coverage](https://img.shields.io/codecov/c/github/spacious-team/table-wrapper-api/develop?label=Coverage&style=flat-square&token=SIOIDt0TcY)](
https://codecov.io/gh/spacious-team/table-wrapper-api)

#### Оглавление
- [Назначение](#назначение)
- [Пример использования](#пример-использования)
- [Зависимости](#зависимости)

### Назначение
Предоставляет единый удобный API для доступа к табличным данным из файлов в форматах excel, xml, csv и др.

Разберем доступ к данным на примере. Пусть на листе excel (xml или csv) имеется несколько таблиц.

- Таблица с ценой товаров:

*Таблица товаров*

| Товар  | Цена (опт), руб/кг | Цена розничная, руб/кг |
|--------|--------------------|------------------------|
| Яблоко | 50                 | 90.5                   |
| Груша  | 120                | 180.0                  |

- Таблица с заголовком из 2-х строк:

*Таблица продаж*

| Покупатель |              | Категория  | Объем, |
|------------|--------------|------------|--------|
| Страна     | Компания     | покупателя | кг     |
| Россия     | "Шестерочка" | опт        | 100000 |
| Беларусь   | "Фруктелла"  | опт        | 50000  |
| Итого      |              |            | 150000 |

- Пусть также иногда встречается следующий вариант заголовка предыдущей таблицы (причем заранее не известно какой вариант
встретится в файле):

| Покупатель |          | Категория  | Вес, |
|------------|----------|------------|------|
| Страна     | Компания | покупателя | кг   |

### Пример использования
Для представленного выше примера объявляются описания столбцов вне зависимости от формата файла (excel, xml, csv и др.):
```java
enum ProductTableHeader implements TableColumnDescription {
    PRODUCT(0),
    PRICE_TRADE("цена", "опт"),
    PRICE("цена", "розничная");

    private final TableColumn column;

    ProductTableHeader(int columnIndex) {
        this.column = ConstantPositionTableColumn.of(columnIndex);
    }

    ProductTableHeader(String... words) {
        this.column = PatternTableColumn.of(words);
    }

    public TableColumn getColumn() {
        return column;
    }   
}

enum SalesTableHeader implements TableColumnDescription {
    BUYER_COUNTRY(MultiLineTableColumn.of("покупатель", "страна")),
    BUYER_COMPANY(MultiLineTableColumn.of("покупатель", "компания")),
    TYPE(MultiLineTableColumn.of("категория", "покупателя")),
    VOLUME(AnyOfTableColumn.of(
                           MultiLineTableColumn.of("объем", "кг"),
                           MultiLineTableColumn.of("вес", "кг")));

    private final TableColumn column;

    CellTableHeader(TableColumn column) {
        this.column = column;
    }

    public TableColumn getColumn() {
        return column;
    }  
}
```
В зависимости от формата исходных данных подготавливаются объекты `ReportPage`. Например, для excel файла потребуются
зависимость [table-wrapper-excel-impl](https://github.com/spacious-team/table-wrapper-excel-impl) или Spring Boot Starter
и код:
```java
TableFactoryRegistry.add(new ExcelTableFactory());             // регистрируем фабрику
Workbook book = new XSSFWorkbook(xlsFileinputStream);          // открываем Excel файл
ReportPage reportPage = new ExcelSheet(book.getSheetAt(0));    // используем 1-ый лист Excel файла для поиска таблиц
```
Используем API для доступа к данным таблиц
```java
// Регистронезависимо найдет ячейку с текстом "Таблица товаров",
// парсит следующую за ней строку как заголовок таблицы,
// оставшиеся строки парсятся как данные до пустой строки или конца файла
Table productTable = reportPage.create("таблица товаров", ProductTableHeader.class);

// Регистронезависимо найдет ячейку с текстом "Таблица продаж",
// парсит следующие за ней 2 строки заголовка таблицы,
// оставшиеся строки парсятся как данные таблицы до строки, содержащей ячейку с текстом "Итого"
Table salesTable = reportPage.create("таблица продаж", "итого",  SalesTableHeader.class, 2);

for (TableRow row : productTable) {
    // Извлечет наименования товаров "Яблоко", "Груша" из "Таблицы товаров"
    String product = row.getStringCellValueOrDefault(PRODUCT, "Неизвестный товар");
    // Извлечет оптовые цены 50 и 120 из "Таблицы товаров"
    BigDecimal price = row.getBigDecimalCellValue(PRICE_TRADE);
}

// Список будет содержать ["Россия", "Беларусь"] из "Таблицы продаж"
Set<String> countries = salesTable.stream()
    .map(row -> row.getStringCellValue(BUYER_COUNTRY))
    .collect(toSet())
```
API предоставляет и другие удобные интерфейсы для работы с таблицами.

### Зависимости
Необходимо подключить репозиторий open source библиотек github [jitpack](https://jitpack.io/#spacious-team/table-wrapper-api),
например для Apache Maven проекта
```xml
<repositories>
    <repository>
        <id>central</id>
        <name>Central Repository</name>
        <url>https://repo.maven.apache.org/maven2</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Далее следует добавить зависимость `table-wrapper-api`
```xml
<dependency>
    <groupId>com.github.spacious-team</groupId>
    <artifactId>table-wrapper-api</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```
В качестве версии можно использовать:
- версию [релиза](https://github.com/spacious-team/table-wrapper-api/releases) на github;
- паттерн `<branch>-SNAPSHOT` для сборки зависимости с последнего коммита выбранной ветки;
- короткий десяти значный номер коммита для сборки зависимости с указанного коммита.
 
Для извлечения данных Вам также потребуется одна или несколько реализаций:
1. [table-wrapper-excel-impl](https://github.com/spacious-team/table-wrapper-excel-impl) для работы с excel файлами
```xml
<dependency>
    <groupId>com.github.spacious-team</groupId>
    <artifactId>table-wrapper-excel-impl</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```
2. [table-wrapper-xml-impl](https://github.com/spacious-team/table-wrapper-xml-impl) для работы с xml файлами
```xml
<dependency>
    <groupId>com.github.spacious-team</groupId>
    <artifactId>table-wrapper-xml-impl</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```
3. [table-wrapper-csv-impl](https://github.com/spacious-team/table-wrapper-csv-impl) для работы с csv (tsv) файлами
```xml
<dependency>
    <groupId>com.github.spacious-team</groupId>
    <artifactId>table-wrapper-csv-impl</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```
Или напишите реализацию для своего формата представления таблицы по аналогии с существующими.
